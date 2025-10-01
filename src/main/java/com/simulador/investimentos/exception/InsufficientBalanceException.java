package com.simulador.investimentos.exception;

public class InsufficientBalanceException extends RuntimeException {
	
	public InsufficientBalanceException() {
		super("Saldo insuficiente para finalizar a operação de compra");
	}

	
	
	public InsufficientBalanceException( String message) {
		super(message);
	}

}
