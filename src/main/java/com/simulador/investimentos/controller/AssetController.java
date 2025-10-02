package com.simulador.investimentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.service.AssetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "AssetController", description = "Operações relacionadas à consulta de ativos")
@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

	private final AssetService assetService;

	public AssetController(AssetService assetService) {
		this.assetService = assetService;
	}
    
	@Operation(summary = "Buscar cotação de ativo", description = "Retorna os dados de cotação de um ativo com base no seu símbolo", responses = {
			@ApiResponse(responseCode = "200", description = "Cotação retornada com sucesso"),
			@ApiResponse(responseCode = "404", description = "Ativo não encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
	})
	
	@GetMapping("/{symbol}")
	public ResponseEntity<QuoteResponseDTO> getAsset(@Parameter(description = "Símbolo do ativo(ex: PETR4, ITUB4, VALE3)") 
	                                                 @PathVariable String symbol) {
		return ResponseEntity.ok(assetService.getAssetData(symbol));
	}
}
