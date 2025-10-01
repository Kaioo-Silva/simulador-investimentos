package com.simulador.investimentos.service.factory;


import java.math.BigDecimal;

import com.simulador.investimentos.entity.User;
import com.simulador.investimentos.entity.Wallet;

public class WalletFactory {

	private static User user = new User();
	

	public static Wallet build() {
		Wallet wallet = new Wallet();
		wallet.setId(1L);
		wallet.setBalance(new BigDecimal (1000));
		wallet.setUser(user);
		user.setId(2L);
		return wallet;
	}
}
