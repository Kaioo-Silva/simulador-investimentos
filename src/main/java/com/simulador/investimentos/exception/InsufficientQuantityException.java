package com.simulador.investimentos.exception;

public class InsufficientQuantityException extends RuntimeException {

	public InsufficientQuantityException( String message) {
		super(message);
	}
}
