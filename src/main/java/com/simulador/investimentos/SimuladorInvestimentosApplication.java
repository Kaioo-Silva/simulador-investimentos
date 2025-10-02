package com.simulador.investimentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(info = @Info(title = "Simulador de Investimentos API", version = "1.0", description = "API para consultar ativos e simular investimentos"))
public class SimuladorInvestimentosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimuladorInvestimentosApplication.class, args);
	}

}
