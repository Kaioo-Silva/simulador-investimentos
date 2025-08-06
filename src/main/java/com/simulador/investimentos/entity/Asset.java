package com.simulador.investimentos.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Asset {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    private String symbol;
    
    private Long quantity;
    
    private Wallet wallet;
}
