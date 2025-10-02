package com.simulador.investimentos.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simulador.investimentos.dtos.AssetDTO;
import com.simulador.investimentos.dtos.OrderDTO;
import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.entity.Order;
import com.simulador.investimentos.entity.OrderType;
import com.simulador.investimentos.entity.Position;
import com.simulador.investimentos.entity.User;
import com.simulador.investimentos.entity.Wallet;
import com.simulador.investimentos.exception.InsufficientBalanceException;
import com.simulador.investimentos.exception.InsufficientQuantityToSellException;
import com.simulador.investimentos.exception.InvalidQuantityToBuyException;
import com.simulador.investimentos.exception.OrderNotFoundException;
import com.simulador.investimentos.exception.ResourceNotFoundException;
import com.simulador.investimentos.exception.UserNotFoundException;
import com.simulador.investimentos.mappers.OrderMapper;
import com.simulador.investimentos.repository.LotAllocationRepository;
import com.simulador.investimentos.repository.OrderRepository;
import com.simulador.investimentos.service.AssetService;
import com.simulador.investimentos.service.LotAllocationService;
import com.simulador.investimentos.service.OrderService;
import com.simulador.investimentos.service.PositionService;
import com.simulador.investimentos.service.UserService;
import com.simulador.investimentos.service.WalletService;
import com.simulador.investimentos.service.factory.AssetDTOFactory;
import com.simulador.investimentos.service.factory.AssetFactory;
import com.simulador.investimentos.service.factory.OrderDTOFactory;
import com.simulador.investimentos.service.factory.OrderFactory;
import com.simulador.investimentos.service.factory.PositionFactory;
import com.simulador.investimentos.service.factory.UserFactory;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;
	@Mock
	private UserService userService;
	@Mock
	private WalletService walletService;
	@Mock
	private AssetService assetService;
	@Mock
	private PositionService positionService;
	@Mock
	private LotAllocationService lotAllocationService;
	@Mock
	private LotAllocationRepository lotAllocationRepository;
	@Mock
	private OrderMapper orderMapper;

	@InjectMocks
	private OrderService orderService;

	@Captor
	private ArgumentCaptor<BigDecimal> balanceArgumentCaptor;
	@Captor
	private ArgumentCaptor<Order> orderArgumentCaptor;
	@Captor
	private ArgumentCaptor<List<Order>> listoOrderArgumentCaptor;

	@Nested
	class executeBuyOrder {

		@Test
		void should_Execute_Buy_Order_When_Everything_Is_Ok() {
			User user = UserFactory.build();
			Asset asset = AssetFactory.build();
			AssetDTO assetDTO = AssetDTOFactory.build();
			List<AssetDTO> quoteResponseResult = List.of(assetDTO);
			QuoteResponseDTO brapiStockData = new QuoteResponseDTO(quoteResponseResult);
			BigDecimal currentPrice = BigDecimal.valueOf(brapiStockData.results().get(0).regularMarketPrice());
			BigDecimal userUpdatedBalance = new BigDecimal(900);
			Position position = new Position(user.getWallet(), asset, 5, currentPrice);
			Order order = new Order(user.getWallet(), asset, 5, OrderType.BUY, currentPrice, position);
			order.setId(8L);

			doReturn(user).when(userService).findUser(user.getId());
			doReturn(brapiStockData).when(assetService).getAssetData("PETR4");
			doReturn(userUpdatedBalance).when(walletService).debitUserBalance(user.getWallet().getBalance(),
					currentPrice, 5);
			doReturn(asset).when(assetService).findOrCreate("PETR4", assetDTO.shortName());
			doReturn(position).when(positionService).createOrUpdatePosition(user.getWallet(), asset, 5, currentPrice);
			doReturn(order).when(orderRepository).save(orderArgumentCaptor.capture());

			OrderDTO result = orderService.executeBuyOrder(user.getId(), "PETR4", 5);

			verify(userService).findUser(user.getId());
			verify(assetService).getAssetData("PETR4");
			verify(orderRepository).save(any());
			verify(assetService).findOrCreate("PETR4", assetDTO.shortName());
			verify(positionService).createOrUpdatePosition(user.getWallet(), asset, 5, currentPrice);
			verify(walletService).debitUserBalance(user.getWallet().getBalance(), currentPrice, 5);
			verify(walletService).saveUpdatedBalance(user.getWallet(), userUpdatedBalance);
			verify(positionService).savePosition(position);
			verify(lotAllocationService).createFromBuyOrder(asset, order, position, 5, currentPrice);

			Order savedOrder = orderArgumentCaptor.getValue();
			assertThat(savedOrder.getAsset()).isEqualTo(asset);
			assertThat(savedOrder.getWallet()).isEqualTo(user.getWallet());
			assertThat(savedOrder.getQuantity()).isEqualTo(5);
			assertThat(savedOrder.getType()).isEqualTo(OrderType.BUY);
			assertThat(savedOrder.getPriceAtExecution()).isEqualTo(currentPrice);
			assertThat(savedOrder.getPosition()).isEqualTo(position);

			assertThat(result.assetSymbol()).isEqualTo(order.getAsset().getSymbol());
			assertThat(result.walletId()).isEqualTo(user.getWallet().getId());
			assertThat(result.quantity()).isEqualTo(order.getQuantity());
			assertThat(result.orderId()).isEqualTo(order.getId());
			assertThat(result.type()).isEqualTo(order.getType());
			assertThat(result.priceAtExecution()).isEqualTo(order.getPriceAtExecution());
			assertThat(result.tradeTime()).isEqualTo(order.getTradeTime());
		}

		@Test
		void should_Throw_Exception_When_Balance_Is_Insufficient() {
			User user = UserFactory.build();
			AssetDTO assetDTO = AssetDTOFactory.build();
			List<AssetDTO> quoteResponseResult = List.of(assetDTO);
			QuoteResponseDTO brapiStockData = new QuoteResponseDTO(quoteResponseResult);

			doReturn(user).when(userService).findUser(user.getId());
			doReturn(brapiStockData).when(assetService).getAssetData("PETR4");
			doThrow(InsufficientBalanceException.class).when(walletService).debitUserBalance(any(), any(), any());

			assertThrows(InsufficientBalanceException.class, () -> {
				orderService.executeBuyOrder(user.getId(), "PETR4", 999);
			});

			verifyNoMoreInteractions(walletService);
		}

		@Test
		void should_Throw_Exception_When_Quantity_Is_Zero_Or_Negative() {
			User user = UserFactory.build();
			AssetDTO assetDTO = AssetDTOFactory.build();
			List<AssetDTO> quoteResponseResult = List.of(assetDTO);
			QuoteResponseDTO brapiStockData = new QuoteResponseDTO(quoteResponseResult);

			doReturn(user).when(userService).findUser(user.getId());
			doReturn(brapiStockData).when(assetService).getAssetData("PETR4");
			doThrow(InvalidQuantityToBuyException.class).when(walletService).debitUserBalance(any(), any(), any());

			assertThrows(InvalidQuantityToBuyException.class, () -> {
				orderService.executeBuyOrder(user.getId(), "PETR4", -1);
			});

			verifyNoMoreInteractions(walletService);
		}

		@Test
		void should_Throw_Exception_When_User_Id_Not_Found() {
			Long nonExistingId = 999L;

			doThrow(UserNotFoundException.class).when(userService).findUser(nonExistingId);

			assertThrows(UserNotFoundException.class, () -> {
				orderService.executeBuyOrder(nonExistingId, "PETR4", 5);
			});
			verify(userService).findUser(nonExistingId);
			verifyNoInteractions(assetService);

		}

		@Test
		void should_Throw_Exception_When_Symbol_Is_Invalid() {

			User user = UserFactory.build();

			doReturn(user).when(userService).findUser(anyLong());

			doThrow(FeignException.class).when(assetService).getAssetData(anyString());

			assertThrows(FeignException.class, () -> {
				orderService.executeBuyOrder(user.getId(), "PETR4", 5);
			});

			verifyNoInteractions(walletService);
		}

	}

	@Nested
	class executeSellOrder {

		@Test
		void should_Sell_Order_When_Everything_Is_Ok() {

			User user = UserFactory.build();
			user.getWallet().setBalance(new BigDecimal("900"));
			Order existingBuyOrder = OrderFactory.buildBuyOrder();
			Order expectedSellOrderResult = OrderFactory.buildSellOrder();
			expectedSellOrderResult.setWallet(user.getWallet());
			AssetDTO assetDTO = AssetDTOFactory.build();
			Position position = PositionFactory.build();
			Integer quantityToSell = 4;
			BigDecimal userBalanceAfterSell = new BigDecimal("980");
			List<AssetDTO> quoteResponseResult = List.of(assetDTO);
			QuoteResponseDTO brapiStockData = new QuoteResponseDTO(quoteResponseResult);
			BigDecimal currentPrice = BigDecimal.valueOf(brapiStockData.results().get(0).regularMarketPrice());

			doReturn(user).when(userService).findUser(user.getId());
			doReturn(Optional.of(existingBuyOrder)).when(orderRepository).findById(existingBuyOrder.getId());
			doReturn(position).when(positionService).findPosition(user.getWallet().getId(),
					existingBuyOrder.getAsset().getSymbol());
			doReturn(brapiStockData).when(assetService).getAssetData(existingBuyOrder.getAsset().getSymbol());
			doReturn(expectedSellOrderResult).when(orderRepository).save(orderArgumentCaptor.capture());

			OrderDTO result = orderService.executeSellOrder(user.getId(), existingBuyOrder.getId(), quantityToSell);

			verify(lotAllocationService).validateAndConsumeForSell(existingBuyOrder.getId(), quantityToSell);
			verify(positionService).removeQuantityFromPosition(user.getWallet().getId(),
					existingBuyOrder.getAsset().getSymbol(), quantityToSell);
			verify(walletService).saveUpdatedBalance(eq(user.getWallet()), balanceArgumentCaptor.capture());
			verify(orderRepository).save(orderArgumentCaptor.capture());

			Order savedOrder = orderArgumentCaptor.getValue();
			assertThat(savedOrder.getAsset()).isEqualTo(existingBuyOrder.getAsset());
			assertThat(savedOrder.getWallet()).isEqualTo(user.getWallet());
			assertThat(savedOrder.getQuantity()).isEqualTo(4);
			assertThat(savedOrder.getType()).isEqualTo(OrderType.SELL);
			assertThat(savedOrder.getPriceAtExecution()).isEqualTo(currentPrice);
			assertThat(savedOrder.getPosition()).isEqualTo(position);

			assertThat(balanceArgumentCaptor.getValue().compareTo(userBalanceAfterSell)).isZero();

			assertThat(result).isNotNull();
			assertThat(result.orderId()).isEqualTo(expectedSellOrderResult.getId());
			assertThat(result.assetSymbol()).isEqualTo(expectedSellOrderResult.getAsset().getSymbol());
			assertThat(result.walletId()).isEqualTo(expectedSellOrderResult.getWallet().getId());
			assertThat(result.type()).isEqualTo(expectedSellOrderResult.getType());
			assertThat(result.quantity()).isEqualTo(quantityToSell);
			assertThat(result.priceAtExecution()).isEqualTo(currentPrice);
			assertThat(result.tradeTime()).isEqualTo(expectedSellOrderResult.getTradeTime());
		}

		@Test
		void should_Throw_Exception_When_User_Id_Not_Found() {
			Long nonExistingId = 999L;

			doThrow(UserNotFoundException.class).when(userService).findUser(nonExistingId);

			assertThrows(UserNotFoundException.class, () -> {
				orderService.executeSellOrder(nonExistingId, 5L, 7);
			});
			verify(userService).findUser(nonExistingId);
			verifyNoInteractions(positionService);
		}

		@Test
		void should_Throw_Exception_When_Order_Id_Does_Not_Exist() {
			User user = UserFactory.build();
			Long nonExistingId = 999L;

			doReturn(user).when(userService).findUser(user.getId());
			doThrow(ResourceNotFoundException.class).when(orderRepository).findById(nonExistingId);

			assertThrows(ResourceNotFoundException.class, () -> {
				orderService.executeSellOrder(user.getId(), nonExistingId, 7);
			});
			verify(orderRepository).findById(nonExistingId);
			verifyNoInteractions(positionService);

		}

		@Test
		void should_Throw_Exception_When_Position_Not_Found() {
			User user = UserFactory.build();
			Order existingBuyOrder = OrderFactory.buildBuyOrder();

			doReturn(user).when(userService).findUser(user.getId());
			doReturn(Optional.of(existingBuyOrder)).when(orderRepository).findById(existingBuyOrder.getId());

			doThrow(ResourceNotFoundException.class).when(positionService).findPosition(user.getWallet().getId(),
					existingBuyOrder.getAsset().getSymbol());

			assertThrows(ResourceNotFoundException.class, () -> {
				orderService.executeSellOrder(user.getId(), existingBuyOrder.getId(), 7);
			});

			verify(positionService).findPosition(user.getWallet().getId(), existingBuyOrder.getAsset().getSymbol());
			verifyNoInteractions(lotAllocationService);

		}

		@Test
		void should_Throw_Exception_When_Quantity_Id_Invalid() {

			User user = UserFactory.build();
			Order existingBuyOrder = OrderFactory.buildBuyOrder();
			Position position = PositionFactory.build();

			doReturn(user).when(userService).findUser(user.getId());
			doReturn(Optional.of(existingBuyOrder)).when(orderRepository).findById(existingBuyOrder.getId());
			doReturn(position).when(positionService).findPosition(user.getWallet().getId(),
					existingBuyOrder.getAsset().getSymbol());

			doThrow(InsufficientQuantityToSellException.class).when(lotAllocationService)
					.validateAndConsumeForSell(existingBuyOrder.getId(), 7);

			assertThrows(InsufficientQuantityToSellException.class, () -> {
				orderService.executeSellOrder(user.getId(), existingBuyOrder.getId(), 7);
			});

			verify(lotAllocationService).validateAndConsumeForSell(existingBuyOrder.getId(), 7);
			verifyNoMoreInteractions(positionService);
		}
	}

		@Nested
		class createAndSaveOrder {

			@Test
			void should_Create_And_Save_Order_Correctly() {
				Wallet wallet = new Wallet(new BigDecimal("1000"));
				Asset asset = AssetFactory.build();
				Integer quantity = 5;
				OrderType type = OrderType.BUY;
				BigDecimal currentPrice = new BigDecimal("10");
				Position position = new Position(wallet, asset, quantity, currentPrice);
				Order orderToSave = new Order(wallet, asset, quantity, type, currentPrice, position);

				doReturn(orderToSave).when(orderRepository).save(any(Order.class));

				Order savedOrder = orderService.createAndSaveOrder(wallet, asset, quantity, type, currentPrice,
						position);

				verify(orderRepository).save(orderArgumentCaptor.capture());
				Order capturedOrder = orderArgumentCaptor.getValue();

				assertThat(savedOrder).isNotNull();
				assertThat(wallet).isEqualTo(savedOrder.getWallet()).isEqualTo(capturedOrder.getWallet());
				assertThat(asset).isEqualTo(savedOrder.getAsset()).isEqualTo(capturedOrder.getAsset());
				assertThat(quantity).isEqualTo(savedOrder.getQuantity()).isEqualTo(capturedOrder.getQuantity());
				assertThat(type).isEqualTo(savedOrder.getType()).isEqualTo(capturedOrder.getType());
				assertThat(currentPrice).isEqualTo(savedOrder.getPriceAtExecution())
						.isEqualTo(capturedOrder.getPriceAtExecution());
				assertThat(position).isEqualTo(savedOrder.getPosition()).isEqualTo(capturedOrder.getPosition());

			}
		}

		@Nested
		class findOrderById {

			@Test
			void should_Return_Order_When_Id_Exists() {
				Order orderToBeFounded = new Order();
				orderToBeFounded.setId(1L);

				Long orderId = orderToBeFounded.getId();

				doReturn(Optional.of(orderToBeFounded)).when(orderRepository).findById(orderId);

				Order result = orderService.findOrderById(orderId);

				assertThat(result).isNotNull();
				assertThat(result.getId()).isEqualTo(orderId);
			}

			@Test
			void should_Throw_Exception_When_Order_Id_Does_Not_Exist() {
				Long nonExistingId = 999L;

				doReturn(Optional.empty()).when(orderRepository).findById(nonExistingId);

				assertThrows(OrderNotFoundException.class, () -> {
					orderService.findOrderById(nonExistingId);
				});
			}
		}

		@Nested
		class getAllOrders {

			@Test
			void should_Return_List_Of_OrderDTOs_When_Orders_Exist() {

				List<Order> orders = List.of(new Order(), new Order());

				List<OrderDTO> expectedDTOs = List.of(OrderDTOFactory.build(), OrderDTOFactory.build());

				doReturn(orders).when(orderRepository).findAll();
				doReturn(expectedDTOs).when(orderMapper).toGetAllUserResponseDTO(orders);

				List<OrderDTO> result = orderService.getAllOrders();

				verify(orderRepository).findAll();
				verify(orderMapper).toGetAllUserResponseDTO(orders);

				assertThat(result).isNotNull();
				assertThat(result).hasSize(2);
				assertThat(result).isEqualTo(expectedDTOs);
			}

			@Test
			void should_Return_Empty_List_When_No_Orders_Exist() {
				List<Order> emptyOrders = Collections.emptyList();
				List<OrderDTO> emptyDTOs = Collections.emptyList();

				doReturn(emptyOrders).when(orderRepository).findAll();
				doReturn(emptyDTOs).when(orderMapper).toGetAllUserResponseDTO(any());

				List<OrderDTO> result = orderService.getAllOrders();

				verify(orderRepository).findAll();
				verify(orderMapper).toGetAllUserResponseDTO(eq(emptyOrders));

				assertThat(result).isNotNull();
				assertThat(result).isEmpty();
			}
		}
	}

