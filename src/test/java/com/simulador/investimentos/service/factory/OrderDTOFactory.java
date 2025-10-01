package com.simulador.investimentos.service.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.simulador.investimentos.dtos.OrderDTO;
import com.simulador.investimentos.entity.OrderType;

public class OrderDTOFactory {

	 public static OrderDTO build() {
	        return new OrderDTO(
	            1L,
	            "PETR4",
	            5L,
	            OrderType.BUY,
	            10,
	            new BigDecimal("100.00"),
	            LocalDateTime.now());
	         }
	        
	        public static OrderDTO buildSellOrder() {
		        return new OrderDTO(
		            1L,
		            "PETR4",
		            5L,
		            OrderType.SELL,
		            8,
		            new BigDecimal("100.00"),
		            LocalDateTime.now()
		        );
	 }
}
