package com.simulador.investimentos.exception;

public class InsufficientQuantityToSellException extends RuntimeException {

	public InsufficientQuantityToSellException(Integer quantity) {
		super("Quantidade de ações insuficiente para venda. Você possui "
			+	quantity + " ações disponíveis para vender desse lote.");
	}
}
