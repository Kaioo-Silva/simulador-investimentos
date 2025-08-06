package com.simulador.investimentos.service;

import org.springframework.stereotype.Service;

import com.simulador.investimentos.client.BrapiClient;
import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.dtos.TradeDTO;
import com.simulador.investimentos.dtos.UserDTO;
import com.simulador.investimentos.entity.OrderType;
import com.simulador.investimentos.entity.Users;

@Service
public class StockService {


	private final BrapiClient brapiClient;
	
	private final UserService userService;

    public StockService(BrapiClient brapiClient,UserService userService) {
        this.brapiClient = brapiClient;
        this.userService= userService;

    }
    
 
    
    
    public QuoteResponseDTO getStockQuote(String symbol) {
        return brapiClient.getQuote(symbol);
    }
}
