package com.simulador.investimentos.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simulador.investimentos.dtos.PositionDTO;
import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.entity.Position;
import com.simulador.investimentos.entity.Wallet;
import com.simulador.investimentos.exception.PositionNotFoundException;
import com.simulador.investimentos.mappers.PositionMapper;
import com.simulador.investimentos.repository.PositionRepository;
import com.simulador.investimentos.service.PositionService;
import com.simulador.investimentos.service.factory.AssetFactory;
import com.simulador.investimentos.service.factory.PositionDTOFactory;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {

	@Mock
	private PositionRepository positionRepository;
	@Mock
	private PositionMapper positionMapper;

	@InjectMocks
	private PositionService positionService;

	@Captor
	private ArgumentCaptor<Position> positionArgumentCaptor;

	@Nested
	class findPosition {

		@Test
		void should_Find_Position_When_Id_Exists() {
			Long id = 1L;
			String symbol = "PETR4";
			Position position = new Position();

			doReturn(Optional.of(position)).when(positionRepository).findByWalletIdAndAssetSymbol(id, symbol);

			Position result = positionService.findPosition(id, symbol);

			assertThat(result).isEqualTo(position);
		}

		@Test
		void should_Throw_Exception_When_Id_Does_Not_Exists() {
			Long nonExistingId = 1L;
			String symbol = "PETR4";

			doReturn(Optional.empty()).when(positionRepository).findByWalletIdAndAssetSymbol(nonExistingId, symbol);

			assertThrows(PositionNotFoundException.class, () -> {
				positionService.findPosition(nonExistingId, symbol);
			});

		}
	}

	@Nested
	class getAllPositions {

		@Test
		void should_Get_All_Positions() {

			Long id = 1L;
			List<Position> positions = List.of(new Position(), new Position());
			List<PositionDTO> expectedDTOs = List.of(PositionDTOFactory.build(), PositionDTOFactory.build());

			doReturn(positions).when(positionRepository).findAllByWalletId(id);
			doReturn(expectedDTOs).when(positionMapper).getAllPositionsDTO(positions);

			List<PositionDTO> result = positionService.getAllPositions(id);

			verify(positionRepository).findAllByWalletId(id);
			verify(positionMapper).getAllPositionsDTO(positions);

			assertThat(result).isNotNull();
			assertThat(result).hasSize(2);
			assertThat(result).isEqualTo(expectedDTOs);
		}
	}

	@Nested
	class savePosition {
		@Test
		void should_Save_Position() {
			Position position = new Position();

			doReturn(position).when(positionRepository).save(position);

			Position result = positionService.savePosition(position);

			verify(positionRepository).save(position);
			assertThat(result).isEqualTo(position);
		}
	}

	@Nested
	class deletePosition {
		@Test
		void should_Delete_Position() {
			Position position = new Position();

			positionService.deletePosition(position);

			verify(positionRepository).delete(position);
		}
	}

	@Nested
	class createOrUpdatePosition {
		@Test
		void should_Create_Position() {
			Wallet wallet = new Wallet();
			wallet.setId(1L);
			Asset asset = AssetFactory.build();
			Integer quantity = 5;
			BigDecimal currentPrice = new BigDecimal("30.00");

			doReturn(Optional.empty()).when(positionRepository).findByWalletIdAndAssetSymbol(wallet.getId(),
					asset.getSymbol());

			Position result = positionService.createOrUpdatePosition(wallet, asset, quantity, currentPrice);

			assertThat(result.getTotalQuantity()).isEqualTo(quantity);
			assertThat(result.getAveragePrice()).isEqualTo(currentPrice);
			assertThat(result.getWallet()).isEqualTo(wallet);
			assertThat(result.getAsset()).isEqualTo(asset);
		}
	}

	@Test
	void should_Update_Existing_Position() {
		Wallet wallet = new Wallet();
		wallet.setId(1L);
		Asset asset = AssetFactory.build();
		Integer existingQuantity = 10;
		BigDecimal existingAvgPrice = new BigDecimal("20.00");

		Position existingPosition = new Position(wallet, asset, existingQuantity, existingAvgPrice);

		Integer quantityToAdd = 5;
		BigDecimal currentPrice = new BigDecimal("30.00");

		doReturn(Optional.of(existingPosition)).when(positionRepository).findByWalletIdAndAssetSymbol(wallet.getId(),
				asset.getSymbol());

		Position result = positionService.createOrUpdatePosition(wallet, asset, quantityToAdd, currentPrice);

		BigDecimal expectedOldTotal = existingAvgPrice.multiply(BigDecimal.valueOf(existingQuantity));
		BigDecimal expectedNewTotal = currentPrice.multiply(BigDecimal.valueOf(quantityToAdd));
		Integer expectedNewQuantity = 15;
		BigDecimal expectedNewAvgPrice = expectedOldTotal.add(expectedNewTotal)
				.divide(BigDecimal.valueOf(expectedNewQuantity), 2, RoundingMode.HALF_UP);

		assertThat(result.getTotalQuantity()).isEqualTo(expectedNewQuantity);
		assertThat(result.getAveragePrice()).isEqualTo(expectedNewAvgPrice);
	}

	@Nested
	class removeQuantityFromPosition {
		@Test
		void should_Remove_Quantity_And_Save_Position() {
			Wallet wallet = new Wallet();
			Long walletId = 1L;
			wallet.setId(walletId);
			Integer quantityToSell = 5;

			Asset asset = AssetFactory.build();

			Position existingPosition = new Position(wallet, asset, 10, new BigDecimal("25.00"));
			Position expectedSavedPosition = new Position(wallet, asset, 5, new BigDecimal("25.00"));

			doReturn(Optional.of(existingPosition)).when(positionRepository).findByWalletIdAndAssetSymbol(walletId,
					asset.getSymbol());
			doReturn(expectedSavedPosition).when(positionRepository).save(positionArgumentCaptor.capture());

			positionService.removeQuantityFromPosition(walletId, asset.getSymbol(), quantityToSell);

			Position positionCaptured = positionArgumentCaptor.getValue();
			assertThat(positionCaptured.getTotalQuantity()).isEqualTo(5);
			assertThat(positionCaptured.getWallet()).isEqualTo(wallet);
			assertThat(positionCaptured.getAsset()).isEqualTo(asset);

			verify(positionRepository).save(positionCaptured);
		}
	}

}
