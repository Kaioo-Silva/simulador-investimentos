package com.simulador.investimentos.mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.simulador.investimentos.dtos.OrderDTO;
import com.simulador.investimentos.entity.Order;
import com.simulador.investimentos.entity.OrderType;

@Component
public class OrderMapper {
	
	public OrderDTO toOrderDTO(Order order) {
		
		Long id = order.getId();
		String assetSymbol = order.getAsset().getSymbol();
		Long walletId = order.getWallet().getId();
		OrderType type = order.getType();
		Integer quantity = order.getQuantity();
		BigDecimal priceAtExecution = order.getPriceAtExecution();
		LocalDateTime tradeTime = order.getTradeTime();
		
		
	 OrderDTO orderDTO = new OrderDTO(id, assetSymbol, walletId, type, quantity, priceAtExecution, tradeTime);	
	 
	 return orderDTO;
	}

	public List<OrderDTO> toGetAllUserResponseDTO(List<Order> orders) {
		List<OrderDTO> orderDTO = orders.stream().map(this::toOrderDTO).collect(Collectors.toList());
		return orderDTO;
	}
	}


