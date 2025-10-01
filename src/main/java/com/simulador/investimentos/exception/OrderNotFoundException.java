package com.simulador.investimentos.exception;

public class OrderNotFoundException extends RuntimeException {
	
	public OrderNotFoundException() {
		super("Ordem não encontrada");
	}
}


