package com.simulador.investimentos.dtos;

import java.time.LocalDateTime;

import com.simulador.investimentos.entity.OrderType;

public record OrderDTO(Long id, String assetSymbol, Long walletId, OrderType type, Integer quantity, Double priceAtExecution, LocalDateTime tradeTime) {
	
	}

	


