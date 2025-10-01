package com.simulador.investimentos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.simulador.investimentos.dtos.OrderDTO;
import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.entity.Order;
import com.simulador.investimentos.entity.OrderType;
import com.simulador.investimentos.entity.Position;
import com.simulador.investimentos.entity.User;
import com.simulador.investimentos.entity.Wallet;
import com.simulador.investimentos.exception.OrderNotFoundException;
import com.simulador.investimentos.mappers.OrderMapper;
import com.simulador.investimentos.repository.OrderRepository;

@Service
public class OrderService {

	private OrderRepository orderRepository;
	private UserService userService;
	private WalletService walletService;
	private AssetService assetService;
	private PositionService positionService;
	private LotAllocationService lotAllocationService;
	private OrderMapper orderMapper;

	public OrderService(OrderRepository orderRepository, UserService userService, WalletService walletService,
			AssetService assetService, PositionService positionService, LotAllocationService lotAllocationService,
			OrderMapper orderMapper) {
		this.orderRepository = orderRepository;
		this.userService = userService;
		this.walletService = walletService;
		this.assetService = assetService;
		this.positionService = positionService;
		this.lotAllocationService = lotAllocationService;
		this.orderMapper = orderMapper;
	}

	public OrderDTO executeBuyOrder(Long userId, String symbol, Integer quantity) {
		User user = userService.findUser(userId);
		Wallet userWallet = user.getWallet();

		QuoteResponseDTO brapiStockData = assetService.getAssetData(symbol);
		String stockName = brapiStockData.results().get(0).shortName();
		BigDecimal currentPrice = BigDecimal.valueOf(brapiStockData.results().get(0).regularMarketPrice());

		BigDecimal userUpdatedBalance = walletService.debitUserBalance(userWallet.getBalance(), currentPrice, quantity);
		walletService.saveUpdatedBalance(userWallet, userUpdatedBalance);

		Asset assetData = assetService.findOrCreate(symbol, stockName);

		Position position = positionService.createOrUpdatePosition(userWallet, assetData, quantity, currentPrice);
		positionService.savePosition(position);

		Order order = createAndSaveOrder(userWallet, assetData, quantity, OrderType.BUY, currentPrice, position);

		lotAllocationService.createFromBuyOrder(assetData, order, position, quantity, currentPrice);

		return new OrderDTO(order.getId(), symbol, userWallet.getId(), order.getType(), order.getQuantity(),
				order.getPriceAtExecution(), order.getTradeTime());
	}

	public OrderDTO executeSellOrder(Long userId, Long orderId, Integer quantityToSell) {
		User user = userService.findUser(userId);
		Wallet userWallet = user.getWallet();
		BigDecimal userBalance = userWallet.getBalance();
		Order order = findOrderById(orderId);

		Position position = positionService.findPosition(userWallet.getId(), order.getAsset().getSymbol());
		lotAllocationService.validateAndConsumeForSell(order.getId(), quantityToSell);
		positionService.removeQuantityFromPosition(userWallet.getId(), order.getAsset().getSymbol(), quantityToSell);

		QuoteResponseDTO brapiStockData = assetService.getAssetData(order.getAsset().getSymbol());
		BigDecimal currentPrice = BigDecimal.valueOf(brapiStockData.results().get(0).regularMarketPrice());

		BigDecimal profitOrLoss = currentPrice.multiply(BigDecimal.valueOf(quantityToSell));
		BigDecimal userBalanceAfterSell = userBalance.add(profitOrLoss);
		walletService.saveUpdatedBalance(userWallet, userBalanceAfterSell);

		Order orderClosed = createAndSaveOrder(userWallet, order.getAsset(), quantityToSell, OrderType.SELL,
				currentPrice, position);
		return new OrderDTO(orderClosed.getId(), orderClosed.getAsset().getSymbol(), userWallet.getId(), OrderType.SELL,
				quantityToSell, currentPrice, orderClosed.getTradeTime());
	}

	public Order createAndSaveOrder(Wallet wallet, Asset asset, Integer quantity, OrderType type,
			BigDecimal currentPrice, Position position) {
		Order order = new Order(wallet, asset, quantity, type, currentPrice, position);
		return orderRepository.save(order);
	}

	public Order findOrderById(Long id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException());
	}

	public List<OrderDTO> getAllOrders() {
		List<Order> orders = orderRepository.findAll();
		List<OrderDTO> orderDTO = orderMapper.toGetAllUserResponseDTO(orders);
		return orderDTO;
	}

}
