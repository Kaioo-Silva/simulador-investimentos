package com.simulador.investimentos.service.factory;

import java.math.BigDecimal;

import com.simulador.investimentos.entity.User;
import com.simulador.investimentos.entity.Wallet;

public class UserFactory {
	
	private static Wallet wallet = new Wallet(new BigDecimal(1000));
	
	
	public static User build() {
		User user = new User();
		user.setId(1L);
		user.setName("John");
		user.setWallet(wallet);
		user.getWallet().setId(2L);
		return user;
	}

}
