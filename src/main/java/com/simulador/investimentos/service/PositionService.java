package com.simulador.investimentos.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.simulador.investimentos.dtos.PositionDTO;
import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.entity.Position;
import com.simulador.investimentos.entity.Wallet;
import com.simulador.investimentos.exception.PositionNotFoundException;
import com.simulador.investimentos.mappers.PositionMapper;
import com.simulador.investimentos.repository.PositionRepository;

@Service
public class PositionService {

	private PositionRepository positionRepository;
	private PositionMapper positionMapper;

	public PositionService(PositionRepository positionRepository, PositionMapper positionMapper) {
		this.positionRepository = positionRepository;
		this.positionMapper = positionMapper;
	}

	public Position findPosition(Long walletId, String assetSymbol) {
		return positionRepository.findByWalletIdAndAssetSymbol(walletId, assetSymbol)
				.orElseThrow(() -> new PositionNotFoundException());
	}
	
	
	public List<PositionDTO> getAllPositions(Long walletId) {

		List<Position> positions = positionRepository.findAllByWalletId(walletId);
		List<PositionDTO> positionsDTO = positionMapper.getAllPositionsDTO(positions);
		return positionsDTO;

	}

	public Position savePosition(Position position) {
		return positionRepository.save(position);
	}

	public void deletePosition(Position position) {
		positionRepository.delete(position);
	}

	public Position createOrUpdatePosition(Wallet wallet, Asset asset, Integer quantity, BigDecimal currentPrice) {
		Position position = positionRepository.findByWalletIdAndAssetSymbol(wallet.getId(), asset.getSymbol())
				.orElse(new Position(wallet, asset, 0, BigDecimal.ZERO));

		Integer oldQuantity = position.getTotalQuantity();
		BigDecimal oldTotal = position.getAveragePrice().multiply(BigDecimal.valueOf(oldQuantity));
		BigDecimal newTotal = currentPrice.multiply(BigDecimal.valueOf(quantity));
		Integer newQuantity = oldQuantity + quantity;
		BigDecimal newAveragePrice = oldTotal.add(newTotal).divide(BigDecimal.valueOf(newQuantity), 2,
				RoundingMode.HALF_UP);

		position.setTotalQuantity(newQuantity);
		position.setAveragePrice(newAveragePrice);

		return position;
	}

	public void removeQuantityFromPosition(Long walletId, String assetSymbol, Integer quantityToSell) {
		Position position = findPosition(walletId, assetSymbol);
		position.setTotalQuantity(position.getTotalQuantity() - quantityToSell);
		positionRepository.save(position);
	}
	
}