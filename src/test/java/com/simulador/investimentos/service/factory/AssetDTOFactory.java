package com.simulador.investimentos.service.factory;


import com.simulador.investimentos.dtos.AssetDTO;

public class AssetDTOFactory {
	
	public static AssetDTO build() {
		AssetDTO assetDTO = new AssetDTO("PETR4","PETROBRAS PN", 20.00);
		return assetDTO;
	}

}
