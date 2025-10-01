package com.simulador.investimentos.mappers;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.simulador.investimentos.dtos.WalletDTO;
import com.simulador.investimentos.entity.Wallet;

@Component
public class WalletMapper {

	public WalletDTO toWalletDTO(Wallet wallet) {

		Long id = wallet.getId();
		BigDecimal balance = wallet.getBalance();
		Long userId = wallet.getUser().getId();
		
		
	 WalletDTO walletDTO = new WalletDTO(id, balance, userId);	
	 return walletDTO;
	}
}
