package com.simulador.investimentos.dtos;

import java.math.BigDecimal;
import java.util.List;

public record WalletDTO(Long id, BigDecimal balance, Long userId, List<Long> orderIds) { // BIG DECIMAL

}
