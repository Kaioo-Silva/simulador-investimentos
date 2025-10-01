package com.simulador.investimentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.service.AssetService;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

	private final AssetService assetService;

	public AssetController(AssetService assetService) {
		this.assetService = assetService;
	}

	@GetMapping("/{symbol}")
	public ResponseEntity<QuoteResponseDTO> getAsset(@PathVariable String symbol) {
		return ResponseEntity.ok(assetService.getAssetData(symbol));
	}

}
