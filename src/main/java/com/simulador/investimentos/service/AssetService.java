package com.simulador.investimentos.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.simulador.investimentos.client.BrapiClient;
import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.entity.Asset;
import com.simulador.investimentos.exception.AssetNotFoundException;
import com.simulador.investimentos.repository.AssetRepository;

import feign.FeignException;

@Service
public class AssetService {

	private AssetRepository assetRepository;
	private BrapiClient brapiClient;

	public AssetService(AssetRepository assetRepository, BrapiClient brapiClient) {
		this.assetRepository = assetRepository;
		this.brapiClient = brapiClient;

	}

	public QuoteResponseDTO getAssetData(String symbol) {
		
		try {
		return brapiClient.getQuote(symbol);
	} catch(FeignException.NotFound ex) {
		throw new AssetNotFoundException();
	}
		
	}

	public Asset findOrCreate(String symbol, String stockName) {
		return assetRepository.findById(symbol).orElseGet(() -> {
			Asset newAsset = new Asset(symbol, stockName, new ArrayList<>());
			return assetRepository.save(newAsset);
		});

	}

}
