package com.simulador.investimentos.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.entity.LotAllocation;
import com.simulador.investimentos.entity.Order;
import com.simulador.investimentos.entity.Position;
import com.simulador.investimentos.exception.InsufficientQuantityToSellException;
import com.simulador.investimentos.exception.ResourceNotFoundException;
import com.simulador.investimentos.repository.LotAllocationRepository;

@Service
public class LotAllocationService {

	private LotAllocationRepository lotAllocationRepository;

	public LotAllocationService(LotAllocationRepository lotAllocationRepository) {
		this.lotAllocationRepository = lotAllocationRepository;
	}

	public LotAllocation createFromBuyOrder(Asset asset, Order buyOrder, Position position, Integer quantity, 
			BigDecimal buyPrice) {

		LotAllocation lot = new LotAllocation(asset, buyOrder, position, quantity, buyPrice); 
		return lotAllocationRepository.save(lot);
	}

	public LotAllocation findLot(Long buyOrderId) {
		LotAllocation lot = lotAllocationRepository.findByBuyOrderId(buyOrderId)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhum lote encontrado para essa ordem de compra"));
		return lot;
	}

	public void validateAndConsumeForSell(Long buyOrderId, Integer quantityToSell) {
		LotAllocation lot = findLot(buyOrderId);

		if (lot.getQuantity() < quantityToSell) {
			throw new InsufficientQuantityToSellException( lot.getQuantity());
		}

		lot.setQuantity(lot.getQuantity() - quantityToSell);
		lotAllocationRepository.save(lot);
	}
}