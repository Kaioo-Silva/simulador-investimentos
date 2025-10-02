package com.simulador.investimentos.unit.controller;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simulador.investimentos.controller.OrderController;
import com.simulador.investimentos.dtos.BuyRequestDTO;
import com.simulador.investimentos.dtos.OrderDTO;
import com.simulador.investimentos.dtos.SellOrderDTO;
import com.simulador.investimentos.entity.Order;
import com.simulador.investimentos.exception.AssetNotFoundException;
import com.simulador.investimentos.exception.InsufficientBalanceException;
import com.simulador.investimentos.exception.InsufficientQuantityToSellException;
import com.simulador.investimentos.exception.InvalidQuantityToBuyException;
import com.simulador.investimentos.exception.OrderNotFoundException;
import com.simulador.investimentos.exception.PositionNotFoundException;
import com.simulador.investimentos.exception.UserNotFoundException;
import com.simulador.investimentos.mappers.OrderMapper;
import com.simulador.investimentos.service.OrderService;
import com.simulador.investimentos.service.factory.OrderDTOFactory;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
	

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;
	
	@MockBean
	private OrderMapper orderMapper;

	@Autowired
	private OrderController orderController;

	@Autowired
	private ObjectMapper objectMapper;

	@Nested
	class buyAsset {

		@Test
		void should_Buy_Asset_When_Everything_Is_Ok()  throws Exception  {
			Long userId = 5L;
			OrderDTO orderDTO = OrderDTOFactory.build();
			BuyRequestDTO buyRequestDTO = new BuyRequestDTO("PETR4", 10);
			String jsonBody = objectMapper.writeValueAsString(buyRequestDTO);
			
			doReturn(orderDTO).when(orderService).executeBuyOrder(userId, buyRequestDTO.assetSymbol(), buyRequestDTO.quantity());
			
			mockMvc.perform(post("/api/v1/orders/buy/" + userId)
					 .contentType(MediaType.APPLICATION_JSON)
					 .content(jsonBody))
			        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			        .andExpect(status().isOk())
			        .andExpect(jsonPath("$.id").value(orderDTO.orderId()))
			        .andExpect(jsonPath("$.assetSymbol").value(orderDTO.assetSymbol()))
			        .andExpect(jsonPath("$.quantity").value(orderDTO.quantity()))
			        .andExpect(jsonPath("$.priceAtExecution", comparesEqualTo(orderDTO.priceAtExecution().doubleValue())));
			}
	
		@Test
		void should_Return_400_When_Balance_Is_Insufficient()  throws Exception  {
			Long userId = 5L;
			BuyRequestDTO buyRequestDTO = new BuyRequestDTO("PETR4", 999);
			String jsonBody = objectMapper.writeValueAsString(buyRequestDTO);
			
			doThrow(new InsufficientBalanceException()).when(orderService).
			           executeBuyOrder(userId, buyRequestDTO.assetSymbol(), buyRequestDTO.quantity());
			
			mockMvc.perform(post("/api/v1/orders/buy/" + userId)
					 .contentType(MediaType.APPLICATION_JSON)
					 .content(jsonBody))
			        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			        .andExpect(status().isBadRequest())
			        .andExpect(jsonPath("$.status").value(400))
		            .andExpect(jsonPath("$.error").value("Bad Request"))
		            .andExpect(jsonPath("$.message").value("Saldo insuficiente para finalizar a operação de compra"));       
		}
		
		@Test
		void should_Return_400_When_Quantity_Is_Invalid()  throws Exception  {
			Long userId = 5L;
			BuyRequestDTO buyRequestDTO = new BuyRequestDTO("PETR4", -1);
			String jsonBody = objectMapper.writeValueAsString(buyRequestDTO);

			
			doThrow(new InvalidQuantityToBuyException()).when(orderService).
			           executeBuyOrder(userId, buyRequestDTO.assetSymbol(), buyRequestDTO.quantity());
			
			mockMvc.perform(post("/api/v1/orders/buy/" + userId)
					 .contentType(MediaType.APPLICATION_JSON)
					 .content(jsonBody))
			        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			        .andExpect(status().isBadRequest())
			        .andExpect(jsonPath("$.status").value(400))
		            .andExpect(jsonPath("$.error").value("Bad Request"))
		            .andExpect(jsonPath("$.message").value("Quantidade de ações inválida para comprar, favor informar um valor maior que 0."));  
		}
		
		@Test
		void should_Return_404_When_User_Id_Is_Invalid()  throws Exception  {
			Long userId = 999L;
			BuyRequestDTO buyRequestDTO = new BuyRequestDTO("PETR4", 5);
			String jsonBody = objectMapper.writeValueAsString(buyRequestDTO);
			
			doThrow(new UserNotFoundException()).when(orderService).
			           executeBuyOrder(userId, buyRequestDTO.assetSymbol(), buyRequestDTO.quantity());
			
			mockMvc.perform(post("/api/v1/orders/buy/" + userId)
					 .contentType(MediaType.APPLICATION_JSON)
					 .content(jsonBody))
			        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			        .andExpect(status().isNotFound())
			        .andExpect(jsonPath("$.status").value(404))
		            .andExpect(jsonPath("$.error").value("Not Found"))
		            .andExpect(jsonPath("$.message").value("Id de usuário informada não existe"));  
		}
		
		@Test
		void should_Return_404_When_Symbol_Is_Invalid() throws Exception {
		    Long userId = 5L;
		    BuyRequestDTO buyRequestDTO = new BuyRequestDTO("INVALID123", 5);
		    String jsonBody = objectMapper.writeValueAsString(buyRequestDTO);

		    doThrow(new AssetNotFoundException()).when(orderService)
		        .executeBuyOrder(userId, buyRequestDTO.assetSymbol(), buyRequestDTO.quantity());

		    mockMvc.perform(post("/api/v1/orders/buy/" + userId)
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(jsonBody))
		        .andExpect(status().isNotFound())
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		        .andExpect(jsonPath("$.status").value(404))
		        .andExpect(jsonPath("$.error").value("Not Found"))
		        .andExpect(jsonPath("$.message").value("A ação informada não existe"));
		}
	}
		
		@Nested
		class sellAsset {

			@Test
			void should_Sell_Asset_When_Everything_Is_Ok()  throws Exception  {
				Long userId = 5L;
				OrderDTO orderDTO = OrderDTOFactory.buildSellOrder();
				SellOrderDTO sellOrderDTO = new SellOrderDTO(1L, 8);
				String jsonBody = objectMapper.writeValueAsString(sellOrderDTO);
				
				doReturn(orderDTO).when(orderService).executeSellOrder(userId, sellOrderDTO.orderId(), sellOrderDTO.quantityToSell());
				
				mockMvc.perform(put("/api/v1/orders/sell/" + userId)
						 .contentType(MediaType.APPLICATION_JSON)
						 .content(jsonBody))
				        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
				        .andExpect(status().isOk())
				        .andExpect(jsonPath("$.id").value(orderDTO.orderId()))
				        .andExpect(jsonPath("$.assetSymbol").value(orderDTO.assetSymbol()))
				        .andExpect(jsonPath("$.quantity").value(orderDTO.quantity()))
				        .andExpect(jsonPath("$.priceAtExecution", comparesEqualTo(orderDTO.priceAtExecution().doubleValue())));
				}

			    @Test
			    void should_Return_404_When_User_Id_Is_Invalid() throws Exception {
			        Long userId = 999L;
			        SellOrderDTO sellOrderDTO = new SellOrderDTO(1L, 8);
			        String jsonBody = objectMapper.writeValueAsString(sellOrderDTO);

			        doThrow(new UserNotFoundException())
			            .when(orderService).executeSellOrder(userId, sellOrderDTO.orderId(), sellOrderDTO.quantityToSell());

			        mockMvc.perform(put("/api/v1/orders/sell/" + userId)
			                    .contentType(MediaType.APPLICATION_JSON)
			                    .content(jsonBody))
			        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			        .andExpect(status().isNotFound())
			        .andExpect(jsonPath("$.status").value(404))
		            .andExpect(jsonPath("$.error").value("Not Found"))
		            .andExpect(jsonPath("$.message").value("Id de usuário informada não existe"));
			    }

			    @Test
			    void should_Return_404_When_Order_Not_Found() throws Exception {
			        Long userId = 5L;
			        SellOrderDTO sellOrderDTO = new SellOrderDTO(999L, 8); 
			        String jsonBody = objectMapper.writeValueAsString(sellOrderDTO);

			        doThrow(new OrderNotFoundException())
			            .when(orderService).executeSellOrder(userId, sellOrderDTO.orderId(), sellOrderDTO.quantityToSell());

			        mockMvc.perform(put("/api/v1/orders/sell/" + userId)
			                    .contentType(MediaType.APPLICATION_JSON)
			                    .content(jsonBody))
			       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			               .andExpect(status().isNotFound())
					        .andExpect(jsonPath("$.status").value(404))
				            .andExpect(jsonPath("$.error").value("Not Found"))
			               .andExpect(jsonPath("$.message").value("Ordem não encontrada"));
			    }

			    @Test
			    void should_Return_404_When_Position_Not_Found() throws Exception {
			        Long userId = 5L;
			        SellOrderDTO sellOrderDTO = new SellOrderDTO(1L, 8); 
			        String jsonBody = objectMapper.writeValueAsString(sellOrderDTO);

			        doThrow(new PositionNotFoundException())
			            .when(orderService).executeSellOrder(userId, sellOrderDTO.orderId(), sellOrderDTO.quantityToSell());

			        mockMvc.perform(put("/api/v1/orders/sell/" + userId)
			                    .contentType(MediaType.APPLICATION_JSON)
			                    .content(jsonBody))
			        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		               .andExpect(status().isNotFound())
				        .andExpect(jsonPath("$.status").value(404))
			            .andExpect(jsonPath("$.error").value("Not Found"))
			               .andExpect(jsonPath("$.message").value("Posição não encontrada"));
			    }

			    @Test
			    void should_Return_400_When_Insufficient_Quantity() throws Exception {
			        Long userId = 5L;
			        Integer userQuantity = 500;
			        SellOrderDTO sellOrderDTO = new SellOrderDTO(1L, 1000); 
			        String jsonBody = objectMapper.writeValueAsString(sellOrderDTO);

			        doThrow(new InsufficientQuantityToSellException(userQuantity))
			            .when(orderService).executeSellOrder(userId, sellOrderDTO.orderId(), sellOrderDTO.quantityToSell());

			        mockMvc.perform(put("/api/v1/orders/sell/" + userId)
			                    .contentType(MediaType.APPLICATION_JSON)
			                    .content(jsonBody))
			        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			               .andExpect(status().isBadRequest())
			               .andExpect(jsonPath("$.status").value(400))
				            .andExpect(jsonPath("$.error").value("Bad Request"))
			               .andExpect(jsonPath("$.message").value("Quantidade de ações insuficiente para venda. Você possui "
			+	userQuantity + " ações disponíveis para vender desse lote."));
			    }
			}

		@Nested
		class findOrder {
			
			@Test
			void should_Find_Order_When_Id_Exists()  throws Exception  {
				Long orderId = 5L;
				Order order = new Order();
				OrderDTO orderDTO = OrderDTOFactory.build();

				doReturn(order).when(orderService).findOrderById(orderId);
				doReturn(orderDTO).when(orderMapper).toOrderDTO(order);
				
				mockMvc.perform(get("/api/v1/orders/" + orderId))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(orderDTO.orderId()))
				.andExpect(jsonPath("$.assetSymbol").value(orderDTO.assetSymbol()))
				.andExpect(jsonPath("$.quantity").value(orderDTO.quantity()));
			}
			
			@Test
			void should_Return_404_When_Order_Not_Found() throws Exception {
			    Long orderId = 999L;

			    doThrow(new OrderNotFoundException())
			        .when(orderService).findOrderById(orderId);

			    mockMvc.perform(get("/api/v1/orders/" + orderId)
			            .accept(MediaType.APPLICATION_JSON))
			            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			            .andExpect(status().isNotFound())
			            .andExpect(jsonPath("$.status").value(404))
			            .andExpect(jsonPath("$.error").value("Not Found"))
	                    .andExpect(jsonPath("$.message").value("Ordem não encontrada"));
			}
			}

			@Nested
			class getAllOrders{
				@Test
				void should_Return_All_Orders() throws Exception {
				List<OrderDTO> expectedDTOs = List.of(OrderDTOFactory.build(), OrderDTOFactory.build());
						
				doReturn(expectedDTOs).when(orderService).getAllOrders();
				
				mockMvc.perform(get("/api/v1/orders"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedDTOs.size()))
				.andExpect(jsonPath("$[0].id").value(expectedDTOs.get(0).orderId()))
				.andExpect(jsonPath("$[1].assetSymbol").value(expectedDTOs.get(1).assetSymbol()));				
			}
		}
	}
		
	  

	

	


