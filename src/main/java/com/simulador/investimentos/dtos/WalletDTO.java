package com.simulador.investimentos.dtos;

import java.util.List;

public record WalletDTO(Long id, Double balance, Long userId, List<Long> orderIds) {

}
