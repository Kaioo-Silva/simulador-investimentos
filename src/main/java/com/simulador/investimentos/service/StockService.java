package com.simulador.investimentos.service;

import org.springframework.stereotype.Service;

import com.simulador.investimentos.client.BrapiClient;
import com.simulador.investimentos.dtos.QuoteResponseDTO;

@Service
public class StockService {

	private final BrapiClient brapiClient;

    public StockService(BrapiClient brapiClient) {
        this.brapiClient = brapiClient;
    }

    public QuoteResponseDTO getStockQuote(String symbol) {
        return brapiClient.getQuote(symbol);
    }
}
