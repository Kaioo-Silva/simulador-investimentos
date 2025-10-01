package com.simulador.investimentos.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.simulador.investimentos.dtos.WalletDTO;
import com.simulador.investimentos.entity.Wallet;
import com.simulador.investimentos.exception.InsufficientBalanceException;
import com.simulador.investimentos.exception.InvalidQuantityToBuyException;
import com.simulador.investimentos.exception.ResourceNotFoundException;
import com.simulador.investimentos.mappers.WalletMapper;
import com.simulador.investimentos.repository.WalletRepository;

@Service
public class WalletService {

	private WalletRepository walletRepository;
	private WalletMapper walletMapper;

	public WalletService(WalletRepository walletRepository, WalletMapper walletMapper) {
		this.walletRepository = walletRepository;
		this.walletMapper = walletMapper;
	}

	public void saveUpdatedBalance(Wallet wallet, BigDecimal userBalanceUpdated) {
		wallet.setBalance(userBalanceUpdated);
		walletRepository.save(wallet);
	}

	public WalletDTO getBalanceValue(Long id) {
		Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Carteira com id " + id + " não existe"));
		WalletDTO walletDTO = walletMapper.toWalletDTO(wallet);
		return walletDTO;
	}

	public BigDecimal debitUserBalance(BigDecimal userBalance, BigDecimal currentPrice, Integer quantity) {

		BigDecimal total = currentPrice.multiply(BigDecimal.valueOf(quantity));

		if (quantity <= 0) {
			throw new InvalidQuantityToBuyException();
		}

		if (userBalance.compareTo(total) < 0) {
			throw new InsufficientBalanceException("Saldo insuficiente para finalizar a operação de compra");
		}

		BigDecimal updatedBalance = userBalance.subtract(total);
		return updatedBalance;
	}

}
