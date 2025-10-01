package com.simulador.investimentos.service.factory;

import java.math.BigDecimal;

import com.simulador.investimentos.entity.Position;
import com.simulador.investimentos.entity.Wallet;

public class PositionFactory {

	private static Wallet wallet = new Wallet(new BigDecimal(900));
	
	public static Position build() {
		Position position = new Position();
		position.setWallet(wallet);
		return position;
	}
}
