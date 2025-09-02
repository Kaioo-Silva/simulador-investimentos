package com.simulador.investimentos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.simulador.investimentos.dtos.BuyRequestDTO;
import com.simulador.investimentos.dtos.OrderDTO;
import com.simulador.investimentos.dtos.SellOrderDTO;
import com.simulador.investimentos.entity.Order;
import com.simulador.investimentos.mappers.OrderMapper;
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
		OrderDTO orderDTO = orderService.executeBuyOrder(userId, request.assetSymbol(), request.quantity());
		return ResponseEntity.ok(orderDTO);
	
	}
	
	@PutMapping("/sell/{userId}")
	public ResponseEntity<OrderDTO> sellAsset(@PathVariable Long userId, @RequestBody SellOrderDTO sellOrderDTO){
		OrderDTO orderDTO = orderService.executeSellOrder(userId, sellOrderDTO.orderId(), sellOrderDTO.quantityToSell());
		return ResponseEntity.ok(orderDTO);
	
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findOrder(@PathVariable Long id){
    	Order order = orderService.findOrderById(id);
    	OrderDTO orderDTO = OrderMapper.toOrderDTO(order);
    	return ResponseEntity.ok(orderDTO);
    }
	
	@GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
    	List<OrderDTO> listOfAllOrders = orderService.getAllOrders();
        return ResponseEntity.ok(listOfAllOrders);
    }
	

}
