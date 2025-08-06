package com.simulador.investimentos.dtos;

import java.time.LocalDateTime;

import com.simulador.investimentos.entity.OrderType;
import com.simulador.investimentos.entity.Users;

public record TradeDTO(String symbol, double regularMarketPrice, Users user, OrderType orderType, LocalDateTime positionTime) {

}
