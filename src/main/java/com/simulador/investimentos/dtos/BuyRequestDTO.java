package com.simulador.investimentos.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyRequestDTO(@Schema(description = "Símbolo do ativo a ser comprado", example = "PETR4") String assetSymbol, Integer quantity) {
	

}
