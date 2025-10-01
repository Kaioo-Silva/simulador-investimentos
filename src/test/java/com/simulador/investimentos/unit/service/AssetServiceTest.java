package com.simulador.investimentos.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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

import com.simulador.investimentos.client.BrapiClient;
import com.simulador.investimentos.dtos.AssetDTO;
import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.repository.AssetRepository;
import com.simulador.investimentos.service.AssetService;
import com.simulador.investimentos.service.factory.AssetDTOFactory;
import com.simulador.investimentos.service.factory.AssetFactory;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)

public class AssetServiceTest {

	@Mock
	private AssetRepository assetRepository;
	@Mock
	private BrapiClient brapiClient;

	@InjectMocks
	private AssetService assetService;
	
	@Captor
	private ArgumentCaptor<Asset> assetArgumentCaptor;

	@Nested
	class getAssetData {

		@Test
		void should_Get_Balance_Value() {
			AssetDTO assetDTO = AssetDTOFactory.build();
			List<AssetDTO> quoteResponseResult = List.of(assetDTO);
			QuoteResponseDTO quoteResponseDTO = new QuoteResponseDTO(quoteResponseResult);

			doReturn(quoteResponseDTO).when(brapiClient).getQuote(assetDTO.symbol());

			QuoteResponseDTO result = assetService.getAssetData(assetDTO.symbol());

			verify(brapiClient).getQuote(assetDTO.symbol());
			assertThat(result).isEqualTo(quoteResponseDTO);

		}

		@Test
		void should_Throw_Exception_When_Symbol_Is_Invalid() {
			String nonExistingSymbol = "PETR44444";

			doThrow(FeignException.class).when(brapiClient).getQuote(nonExistingSymbol);

			assertThrows(FeignException.class, () -> {
				assetService.getAssetData(nonExistingSymbol);
			});

			verify(brapiClient).getQuote(nonExistingSymbol);
		}

	}

	@Nested
	class findOrCreate {

		@Test
		void should_Find_Asset() {
			Asset asset = AssetFactory.build();
			
			doReturn(Optional.of(asset)).when(assetRepository).findById(asset.getSymbol());
			
			Asset result = assetService.findOrCreate(asset.getSymbol(), asset.getName());
			
			assertThat(result).isEqualTo(asset);
			
			verify(assetRepository).findById(asset.getSymbol());
			verifyNoMoreInteractions(assetRepository);
		}
		
		@Test
		void should_Create_Asset() {
			Asset asset = AssetFactory.build();
			
			doReturn(Optional.empty()).when(assetRepository).findById(asset.getSymbol());
			doReturn(asset).when(assetRepository).save(assetArgumentCaptor.capture());
			
			Asset result = assetService.findOrCreate(asset.getSymbol(), asset.getName());
			
			Asset assetCaptured = assetArgumentCaptor.getValue();
			assertThat(result).isEqualTo(asset);
			assertThat(assetCaptured.getName()).isEqualTo(asset.getName());
			assertThat(assetCaptured.getSymbol()).isEqualTo(asset.getSymbol());
			assertThat(assetCaptured.getOrders()).isEqualTo(asset.getOrders());
			
			verify(assetRepository).save(any(Asset.class));
			
		}
	}
}