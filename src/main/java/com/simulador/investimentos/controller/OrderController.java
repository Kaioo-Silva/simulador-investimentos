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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "OrderController", description = "Operações relacionadas à compra, venda e consulta de ordens de ativos")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

	private OrderService orderService;
	private OrderMapper orderMapper;

	public OrderController(OrderService orderService, OrderMapper orderMapper) {
		this.orderService = orderService;
		this.orderMapper = orderMapper;
	}

	@Operation(summary = "Efetua compra de um ativo", description = "Retorna as informações completas da compra efetuada", responses = {
			@ApiResponse(responseCode = "200", description = "Compra efetuada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Saldo insuficiente ou quantidade de ações inválida", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário ou ativo não encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
	})
	@PostMapping("/buy/{userId}")
	public ResponseEntity<OrderDTO> buyAsset(@PathVariable Long userId, @RequestBody BuyRequestDTO request) {
		OrderDTO orderDTO = orderService.executeBuyOrder(userId, request.assetSymbol(), request.quantity());
		return ResponseEntity.ok(orderDTO);
	}

	@Operation(summary = "Efetua venda de um ativo", description = "Retorna as informações completas da venda efetuada", responses = {
			@ApiResponse(responseCode = "200", description = "Venda efetuada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Quantidade de ações insuficiente para venda", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário, ordem ou posição não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
	})
	@PutMapping("/sell/{userId}")
	public ResponseEntity<OrderDTO> sellAsset(@PathVariable Long userId, @RequestBody SellOrderDTO sellOrderDTO) {
		OrderDTO orderDTO = orderService.executeSellOrder(userId, sellOrderDTO.orderId(),
				sellOrderDTO.quantityToSell());
		return ResponseEntity.ok(orderDTO);
	}

	@Operation(summary = "Buscar ordem por ID", description = "Retorna os dados completos de uma ordem com base no seu ID", responses = {
	        @ApiResponse(responseCode = "200", description = "Ordem encontrada com sucesso"),
			@ApiResponse(responseCode = "404", description = "Ordem não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content) 
	})
	@GetMapping("/{id}")
	public ResponseEntity<OrderDTO> findOrderById(@PathVariable Long id) {
		Order order = orderService.findOrderById(id);
		OrderDTO orderDTO = orderMapper.toOrderDTO(order);
		return ResponseEntity.ok(orderDTO);
	}
	@Operation(summary = "Listar todas as ordens", description = "Retorna uma lista com todas as ordens", responses = {
	        @ApiResponse(responseCode = "200", description = "Lista de ordens retornada com sucesso"),
			@ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content) 
	})
	@GetMapping
	public ResponseEntity<List<OrderDTO>> getAllOrders() {
		List<OrderDTO> listOfAllOrders = orderService.getAllOrders();
		return ResponseEntity.ok(listOfAllOrders);
	}

}
