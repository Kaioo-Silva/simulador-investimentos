package com.simulador.investimentos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class BrapiConfig {
	
	@Bean
	public RequestInterceptor brapiAuthInterceptor() {
		String brapiToken = System.getenv("BRAPI_TOKEN");
		return requestTemplate -> {
			requestTemplate.header("Authorization","Bearer " + brapiToken);
		};
	}
}
