package com.simulador.investimentos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.simulador.investimentos.dtos.QuoteResponseDTO;

@FeignClient(name = "brapi", url = "https://brapi.dev/api", configuration = com.simulador.investimentos.config.BrapiConfig.class)

public interface BrapiClient {

	  @GetMapping("/quote/{symbol}")
	  QuoteResponseDTO getQuote(@PathVariable("symbol") String symbol);
	}

