package com.simulador.investimentos.exception;

public class InvalidQuantityToBuyException extends RuntimeException{
	
	public InvalidQuantityToBuyException() {
		super("Quantidade de ações inválida para comprar, favor informar um valor maior que 0.");
	}

}
