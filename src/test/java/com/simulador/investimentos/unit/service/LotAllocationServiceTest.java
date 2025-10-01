package com.simulador.investimentos.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.entity.LotAllocation;
import com.simulador.investimentos.entity.Order;
import com.simulador.investimentos.entity.Position;
import com.simulador.investimentos.exception.InsufficientQuantityToSellException;
import com.simulador.investimentos.exception.ResourceNotFoundException;
import com.simulador.investimentos.repository.LotAllocationRepository;
import com.simulador.investimentos.service.LotAllocationService;

@ExtendWith(MockitoExtension.class)
public class LotAllocationServiceTest {

	@Mock
	private LotAllocationRepository lotAllocationRepository;

	@InjectMocks
	private LotAllocationService lotAllocationService;

	@Captor
	private ArgumentCaptor<LotAllocation> lotArgumentCaptor;

	@Nested
	class createFromBuyOrder {

		@Test
		void should_Create_LotAllocation_From_Buy_Order() {
			Asset asset = new Asset();
			Order order = new Order();
			Position position = new Position();
			Integer quantity = 5;
			BigDecimal buyPrice = new BigDecimal(20);
			LotAllocation lotAllocation = new LotAllocation(asset, order, position, quantity, buyPrice);

			doReturn(lotAllocation).when(lotAllocationRepository).save(lotArgumentCaptor.capture());

			LotAllocation result = lotAllocationService.createFromBuyOrder(asset, order, position, quantity, buyPrice);

			LotAllocation lotAllocationCaptor = lotArgumentCaptor.getValue();
			assertThat(result).isEqualTo(lotAllocation);
			assertThat(lotAllocationCaptor.getAsset()).isEqualTo(lotAllocation.getAsset());
			assertThat(lotAllocationCaptor.getBuyOrder()).isEqualTo(lotAllocation.getBuyOrder());
			assertThat(lotAllocationCaptor.getPosition()).isEqualTo(lotAllocation.getPosition());
			assertThat(lotAllocationCaptor.getQuantity()).isEqualTo(lotAllocation.getQuantity());
			assertThat(lotAllocationCaptor.getBuyPrice()).isEqualTo(lotAllocation.getBuyPrice());

		}
	}

		@Nested
		class findLot {

			@Test
			void should_Find_Lot_When_Id_Exists() {
				LotAllocation lotAllocation = new LotAllocation();
				Long id = 1L;

				doReturn(Optional.of(lotAllocation)).when(lotAllocationRepository).findByBuyOrderId(id);

				LotAllocation result = lotAllocationService.findLot(id);

				assertThat(result).isEqualTo(lotAllocation);
			}

			@Test
			void should_Throw_Exception_When_LotAllocation_Id_Not_Found() {
				Long nonExistingId = 1L;

				doReturn(Optional.empty()).when(lotAllocationRepository).findByBuyOrderId(nonExistingId);

				assertThrows(ResourceNotFoundException.class, () -> {
					lotAllocationService.findLot(nonExistingId);

				});
			}

		}

		@Nested
		class validateAndConsumeForSell {

			@Test
			void should_Validate_The_Operation() {
				LotAllocation lotAllocation = new LotAllocation();
				Long id = 1L;
				lotAllocation.setQuantity(15);

				doReturn(Optional.of(lotAllocation)).when(lotAllocationRepository).findByBuyOrderId(id);
				doReturn(lotAllocation).when(lotAllocationRepository).save(any());

				lotAllocationService.validateAndConsumeForSell(id, 5);

				verify(lotAllocationRepository).save(lotArgumentCaptor.capture());

				LotAllocation capturedLot = lotArgumentCaptor.getValue();
				assertThat(capturedLot.getQuantity()).isEqualTo(10);
				assertThat(capturedLot).isSameAs(lotAllocation);
			}

			@Test
			void should_Throw_Exception_When_Quantity_Is_Invalid() {
				LotAllocation lotAllocation = new LotAllocation();
				Long id = 1L;
				lotAllocation.setQuantity(2);

				doReturn(Optional.of(lotAllocation)).when(lotAllocationRepository).findByBuyOrderId(id);

				assertThrows(InsufficientQuantityToSellException.class, () -> {
					lotAllocationService.validateAndConsumeForSell(id, 5);
				});
				verifyNoMoreInteractions(lotAllocationRepository);

			}

		}
	}

