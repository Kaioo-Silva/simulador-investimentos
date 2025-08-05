package com.simulador.investimentos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.service.StockService;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {
	
	private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{symbol}")
    public QuoteResponseDTO getStock(@PathVariable String symbol) {
        return stockService.getStockQuote(symbol);
    }

}
