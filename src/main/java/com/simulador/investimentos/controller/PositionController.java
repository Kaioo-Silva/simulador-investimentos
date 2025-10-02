package com.simulador.investimentos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.investimentos.dtos.PositionDTO;
import com.simulador.investimentos.entity.Position;
import com.simulador.investimentos.mappers.PositionMapper;
import com.simulador.investimentos.service.PositionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "PositionController", description = "Operações relacionadas às posições de ativos nas carteiras")
@RestController
@RequestMapping("/api/v1/positions")
public class PositionController {

	private PositionService positionService;
	private PositionMapper positionMapper;

	public PositionController(PositionService positionService, PositionMapper positionMapper) {
		this.positionService = positionService;
		this.positionMapper = positionMapper;
	}

	@Operation(summary = "Buscar posição de um ativo em uma carteira", 
	    description = "Retorna os dados da posição de um ativo específico dentro de uma carteira, com base no ID da carteira e símbolo do ativo", responses = {
		@ApiResponse(responseCode = "200", description = "Posição retornada com sucesso"),
		@ApiResponse(responseCode = "404", description = "Posição não encontrada", content = @Content),
		@ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
	})
	
	@GetMapping("/wallets/{walletId}")
	public ResponseEntity<PositionDTO> getPosition(@PathVariable Long walletId, @RequestParam String assetSymbol) { 
		Position position = positionService.findPosition(walletId, assetSymbol);
		PositionDTO positionDTO = positionMapper.toPositionDTO(position);
		return ResponseEntity.ok(positionDTO);
	}

	@Operation(summary = "Retorna todas as posições de uma carteira", 
		description = "Retorna todas as posições de ativos associadas a uma carteira", responses = {
		@ApiResponse(responseCode = "200", description = "Posições retornadas com sucesso"),
		@ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
	})
	
	@GetMapping("/allpositions/wallets/{walletId}") 
	public ResponseEntity<List<PositionDTO>> getAllPositions(@PathVariable Long walletId) {
		return ResponseEntity.ok(positionService.getAllPositions(walletId));
	}
	
}