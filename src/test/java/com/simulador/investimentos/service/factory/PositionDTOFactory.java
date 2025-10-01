package com.simulador.investimentos.service.factory;

import java.math.BigDecimal;

import com.simulador.investimentos.dtos.PositionDTO;

public class PositionDTOFactory {
	
	public static PositionDTO build() {
		PositionDTO positionDTO = new PositionDTO(1L, 2L, "PETR4", 5, new BigDecimal(1000));
		return positionDTO;
	}

}
