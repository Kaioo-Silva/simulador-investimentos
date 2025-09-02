package com.simulador.investimentos.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.simulador.investimentos.entity.Wallet;
import com.simulador.investimentos.repository.WalletRepository;

@Service
public class WalletService {
	
	private WalletRepository walletRepository;

	public WalletService(WalletRepository walletRepository) {
		this.walletRepository = walletRepository;
	}
	
	public Wallet saveUpdatedBalance(Wallet wallet, BigDecimal userBalanceUpdated) { 
		wallet.setBalance(userBalanceUpdated);
		return walletRepository.save(wallet);
	}
	


}
