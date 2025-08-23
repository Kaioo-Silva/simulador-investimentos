package com.simulador.investimentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.investimentos.dtos.BuyRequestDTO;
import com.simulador.investimentos.dtos.OrderDTO;
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

}
