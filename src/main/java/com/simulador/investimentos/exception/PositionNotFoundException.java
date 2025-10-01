package com.simulador.investimentos.exception;

public class PositionNotFoundException extends RuntimeException {
	
	public PositionNotFoundException() {
		super("Posição não encontrada");
	}
}
