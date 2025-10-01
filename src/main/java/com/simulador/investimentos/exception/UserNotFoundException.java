package com.simulador.investimentos.exception;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
		super("Id de usuário informada não existe");
	}
}
