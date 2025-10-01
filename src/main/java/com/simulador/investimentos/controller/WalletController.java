package com.simulador.investimentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.investimentos.dtos.WalletDTO;
import com.simulador.investimentos.service.WalletService;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

	private WalletService walletService;

	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<WalletDTO> getWalletBalance(@PathVariable Long id) {
		return ResponseEntity.ok(walletService.getBalanceValue(id));
	}
}
