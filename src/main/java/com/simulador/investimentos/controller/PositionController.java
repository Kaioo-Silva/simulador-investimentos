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

@RestController
@RequestMapping("/api/v1/positions")
public class PositionController {

	private PositionService positionService;
	private PositionMapper positionMapper;

	public PositionController(PositionService positionService, PositionMapper positionMapper) {
		this.positionService = positionService;
		this.positionMapper = positionMapper;
	}

	@GetMapping("/wallets/{walletId}")
	public ResponseEntity<PositionDTO> getPosition(@PathVariable Long walletId, @RequestParam String assetSymbol) { 
		Position position = positionService.findPosition(walletId, assetSymbol);
		PositionDTO positionDTO = positionMapper.toPositionDTO(position);
		return ResponseEntity.ok(positionDTO);
	}



	@GetMapping("/allpositions/wallets/{walletId}") 
	public ResponseEntity<List<PositionDTO>> getAllPositions(@PathVariable Long walletId) {
		return ResponseEntity.ok(positionService.getAllPositions(walletId));
	}
	
}