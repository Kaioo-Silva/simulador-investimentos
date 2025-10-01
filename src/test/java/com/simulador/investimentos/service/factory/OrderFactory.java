package com.simulador.investimentos.service.factory;

import java.time.LocalDateTime;

import com.simulador.investimentos.entity.Order;
import com.simulador.investimentos.entity.OrderType;


public class OrderFactory {
	
	public static Order buildBuyOrder() {
		Order order = new Order();
		order.setId(3L);
		order.setAsset(AssetFactory.build());
		return order;
	}
	
	public static Order buildSellOrder() {
		Order order = new Order();
		order.setId(4L);
		order.setAsset(AssetFactory.build());
		order.setType(OrderType.SELL);
		order.setTradeTime(LocalDateTime.now());
		return order;
	}
	
}
