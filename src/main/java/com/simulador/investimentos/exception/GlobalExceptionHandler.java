package com.simulador.investimentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.simulador.investimentos.dtos.ErrorResponseDTO;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {
		
		@ExceptionHandler(InsufficientBalanceException.class)
		public ResponseEntity<ErrorResponseDTO> handleInsufficientBalanceException(InsufficientBalanceException exception){
			ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
					HttpStatus.BAD_REQUEST.value(),
					HttpStatus.BAD_REQUEST.getReasonPhrase(),
					exception.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
		}

		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException exception){
			ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
					HttpStatus.NOT_FOUND.value(),
					HttpStatus.NOT_FOUND.getReasonPhrase(),
					exception.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
		}
		
		@ExceptionHandler(FeignException.class)
		public ResponseEntity<ErrorResponseDTO> handleAssetSymbolNotFoundException(FeignException exception){
			ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
					HttpStatus.NOT_FOUND.value(),
					HttpStatus.NOT_FOUND.getReasonPhrase(),
					"A ação informada não existe");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
	}
		
		@ExceptionHandler(Exception.class)
		public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception exception) {
		    ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
		            HttpStatus.INTERNAL_SERVER_ERROR.value(),
		            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
		            "Ocorreu um erro inesperado. Tente novamente mais tarde.");
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
		}
}
