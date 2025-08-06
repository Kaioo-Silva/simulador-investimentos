package com.simulador.investimentos.dtos;

import com.simulador.investimentos.entity.OrderType;

public record UserDTO(Long id, String name, double balance, OrderType orderType) {

}
