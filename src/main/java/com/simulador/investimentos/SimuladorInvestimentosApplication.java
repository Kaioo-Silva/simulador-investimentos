package com.simulador.investimentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SimuladorInvestimentosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimuladorInvestimentosApplication.class, args);
	}

}
