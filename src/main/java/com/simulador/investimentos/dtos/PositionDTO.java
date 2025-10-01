package com.simulador.investimentos.dtos;

import java.math.BigDecimal;

public record PositionDTO(Long id, Long walletId, String assetSymbol, Integer totalQuantity, BigDecimal averagePrice) {

}
