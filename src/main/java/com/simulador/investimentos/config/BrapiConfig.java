package com.simulador.investimentos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class BrapiConfig {
	
	  @Value("${BRAPI_TOKEN}")
	    private String brapiToken;
	
	@Bean
	public RequestInterceptor brapiAuthInterceptor() {
		return requestTemplate -> {
			requestTemplate.header("Authorization","Bearer " + brapiToken);
		};
	}
}
