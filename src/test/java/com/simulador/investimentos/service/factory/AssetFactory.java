package com.simulador.investimentos.service.factory;

import java.util.ArrayList;

import com.simulador.investimentos.entity.Asset;

public class AssetFactory {

	public static Asset build() {
		Asset asset = new Asset();
		asset.setSymbol("PETR4");
		asset.setName("PETROBRAS PN");
		asset.setOrders(new ArrayList<>());
		return asset;
	}
}
