package com.simulador.investimentos.dtos;

import java.time.LocalDateTime;

import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.entity.OrderType;
import com.simulador.investimentos.entity.Wallet;

public record OrderDTO(Long id, String assetSymbol, Long walletId, OrderType type, Integer quantity, Double priceAtExecution, LocalDateTime tradeTime) {

	//public OrderDTO(Long id2, Asset asset, Wallet wallet, OrderType type2, Integer quantity2, Double priceAtExecution2,
	//		LocalDateTime tradeTime2) {
		// TODO Auto-generated constructor stub
	}

	


