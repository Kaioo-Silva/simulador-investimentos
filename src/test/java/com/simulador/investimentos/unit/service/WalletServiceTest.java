package com.simulador.investimentos.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simulador.investimentos.dtos.WalletDTO;
import com.simulador.investimentos.entity.Wallet;
import com.simulador.investimentos.exception.InsufficientBalanceException;
import com.simulador.investimentos.exception.InvalidQuantityToBuyException;
import com.simulador.investimentos.exception.ResourceNotFoundException;
import com.simulador.investimentos.mappers.WalletMapper;
import com.simulador.investimentos.repository.WalletRepository;
import com.simulador.investimentos.service.WalletService;
import com.simulador.investimentos.service.factory.WalletFactory;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

	@Mock
	private WalletRepository walletRepository;

	@Mock
	private WalletMapper walletMapper;

	@InjectMocks
	private WalletService walletService;

	@Nested
	class saveUpdatedBalance {

		@Test
		void should_Save_Updated_Balance() {
			Wallet wallet = new Wallet(new BigDecimal(1000));
			BigDecimal userUpdatedBalance = new BigDecimal(900);

			doReturn(wallet).when(walletRepository).save(wallet);

			walletService.saveUpdatedBalance(wallet, userUpdatedBalance);

			verify(walletRepository).save(wallet);

			assertThat(wallet.getBalance()).isEqualTo(userUpdatedBalance);
		}
	}

	@Nested
	class getBalanceValue {

		@Test
		void should_Get_Balance_Value() {
			Wallet wallet = WalletFactory.build();
			WalletDTO walletDTO = new WalletDTO(1L, new BigDecimal(1000), 2L);
			
			doReturn(Optional.of(wallet)).when(walletRepository).findById(wallet.getId());
			doReturn(walletDTO).when(walletMapper).toWalletDTO(wallet);
			
			WalletDTO result = walletService.getBalanceValue(walletDTO.id());
			
			assertThat(result).isNotNull();
			assertThat(result.balance()).isEqualTo(wallet.getBalance());
			assertThat(result.id()).isEqualTo(wallet.getId());
			assertThat(result.userId()).isEqualTo(wallet.getUser().getId());	
		}
		

		@Test
		void should_Throw_Exception_When_Wallet_Id_Not_Found() {
			Long nonExistingId = 999L;
			
			doThrow(ResourceNotFoundException.class).when(walletRepository).findById(nonExistingId);
			
			assertThrows(ResourceNotFoundException.class, () -> {
				walletService.getBalanceValue(nonExistingId);
			});
			verify(walletRepository).findById(nonExistingId);
			verifyNoInteractions(walletMapper);
			
	
		}
	}
		
	@Nested
	class debitUserBalance {

		@Test
		void should_Debit_User_Balance() {
			BigDecimal userBalance = new BigDecimal("1000.00");
			BigDecimal currentPrice = new BigDecimal("10.00");
			Integer quantity = 5;

			BigDecimal result = walletService.debitUserBalance(userBalance, currentPrice, quantity);

			BigDecimal expected = userBalance.subtract(currentPrice.multiply(BigDecimal.valueOf(quantity)));
			assertThat(result).isEqualTo(expected);
		}

		@Test
		void should_Throw_Exception_When_Quantity_Is_Invalid() {
			BigDecimal userBalance = new BigDecimal("1000.00");
			BigDecimal currentPrice = new BigDecimal("10.00");
			Integer quantity = -1;

			assertThrows(InvalidQuantityToBuyException.class,
					() -> walletService.debitUserBalance(userBalance, currentPrice, quantity));
		}

		@Test
		void should_Throw_Exception_When_Balance_Is_Insufficient() {
			BigDecimal userBalance = new BigDecimal("1000.00");
			BigDecimal currentPrice = new BigDecimal("10.00");
			Integer quantity = 101;

			assertThrows(InsufficientBalanceException.class,
					() -> walletService.debitUserBalance(userBalance, currentPrice, quantity));
		}
	}
}
