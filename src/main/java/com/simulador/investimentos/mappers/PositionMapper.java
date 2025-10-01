package com.simulador.investimentos.mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.simulador.investimentos.dtos.PositionDTO;
import com.simulador.investimentos.entity.Position;

@Component
public class PositionMapper {
	
	public PositionDTO toPositionDTO(Position position) {

		Long positionId = position.getId();
		Long walletId = position.getWallet().getId();
		String assetSymbol = position.getAsset().getSymbol();
		Integer totalQuantity = position.getTotalQuantity();
		BigDecimal averagePrice = position.getAveragePrice();
		
		PositionDTO positionDTO = new PositionDTO(positionId, walletId, assetSymbol, totalQuantity, averagePrice);	
	    return positionDTO;
	}
	
	public List<PositionDTO> getAllPositionsDTO(List<Position> position){
		List<PositionDTO> positionsDTO = position.stream().map(this::toPositionDTO).collect(Collectors.toList());
		return positionsDTO;
	}

}
