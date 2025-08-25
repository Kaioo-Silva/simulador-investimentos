package com.simulador.investimentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.investimentos.dtos.BuyRequestDTO;
import com.simulador.investimentos.dtos.OrderClosedDTO;
import com.simulador.investimentos.dtos.OrderDTO;
import com.simulador.investimentos.dtos.SellOrderDTO;
import com.simulador.investimentos.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
	
	private OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@PostMapping("/buy/{userId}")
	public ResponseEntity<OrderDTO> buyAsset(@PathVariable Long userId, @RequestBody BuyRequestDTO request){
		OrderDTO orderDTO = orderService.buy(userId, request.assetSymbol(), request.quantity());
		return ResponseEntity.ok(orderDTO);
	
	}
	
	@PutMapping("/sell/{userId}")
	public ResponseEntity<OrderClosedDTO> sellOpenOrder(@PathVariable Long userId, @RequestBody SellOrderDTO sellOrderDTO){
		OrderClosedDTO orderClosedDTO = orderService.sellOpenPosition(userId, sellOrderDTO.orderId(), sellOrderDTO.quantityStocksWishToSell());
		return ResponseEntity.ok(orderClosedDTO);
	
	}

}
