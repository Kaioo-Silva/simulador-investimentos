package com.simulador.investimentos.dtos;

import java.time.LocalDateTime;

public record ErrorResponseDTO(LocalDateTime timestamp, Integer status, String error, String message) {

	public ErrorResponseDTO(Integer status, String error, String message) {
		this(LocalDateTime.now(), status, error, message);
	}
}
