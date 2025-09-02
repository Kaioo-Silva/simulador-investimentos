package com.simulador.investimentos.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.simulador.investimentos.entity.OrderType;

public record OrderDTO(Long id, String assetSymbol, Long walletId, OrderType type, Integer quantity, BigDecimal priceAtExecution, LocalDateTime tradeTime) { 
	
	}

	


