package com.simulador.investimentos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simulador.investimentos.entity.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long>{
	Optional<Position> findByWalletId(Long walletId);
	List<Position> findAllByWalletId(Long walletId);
	Optional<Position> findByWalletIdAndAssetSymbol(Long walletId, String assetSymbol);

}
