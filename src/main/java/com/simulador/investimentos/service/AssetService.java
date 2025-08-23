package com.simulador.investimentos.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.simulador.investimentos.client.BrapiClient;
import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.repository.AssetRepository;

@Service
public class AssetService {

	private AssetRepository assetRepository;
	private BrapiClient brapiClient;

	public AssetService(AssetRepository assetRepository, BrapiClient brapiClient) {
		this.assetRepository = assetRepository;
		this.brapiClient = brapiClient;

	}

	public QuoteResponseDTO getAssetData(String symbol) {
		return brapiClient.getQuote(symbol);
	}

	public Asset findOrCreate(String symbol, String stockName, Double stockPrice) {
		return assetRepository.findById(symbol).orElseGet(() -> {
			Asset newAsset = new Asset(symbol, stockName, stockPrice, new ArrayList<>());
			return assetRepository.save(newAsset);
		});

	}

}
