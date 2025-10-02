package com.simulador.investimentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.investimentos.dtos.WalletDTO;
import com.simulador.investimentos.service.WalletService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "WalletController", description = "Operações relacionadas às carteiras dos usuários")
@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

	private WalletService walletService;

	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}

	@Operation(summary = "Consultar saldo da carteira", description = "Retorna o saldo total disponível de uma carteira com base no ID informado")
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Saldo da carteira retornado com sucesso"),
	        @ApiResponse(responseCode = "404", description = "Carteira não encontrada", content = @Content),
	        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content) 
	})
	@GetMapping("/{id}")
	public ResponseEntity<WalletDTO> getWalletBalance(@PathVariable Long id) {
		return ResponseEntity.ok(walletService.getBalanceValue(id));
	}
}
