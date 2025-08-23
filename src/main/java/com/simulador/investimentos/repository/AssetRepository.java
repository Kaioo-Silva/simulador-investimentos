package com.simulador.investimentos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simulador.investimentos.entity.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {
    Optional<Asset> findBySymbol(String symbol);
    
    }

