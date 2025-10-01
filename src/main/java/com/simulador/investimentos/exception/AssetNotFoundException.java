package com.simulador.investimentos.exception;

public class AssetNotFoundException extends RuntimeException {

	public AssetNotFoundException() {
		super("A ação informada não existe");
	}

}
