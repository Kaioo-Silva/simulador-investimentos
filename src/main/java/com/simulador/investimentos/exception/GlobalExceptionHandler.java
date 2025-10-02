package com.simulador.investimentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.simulador.investimentos.dtos.ErrorResponseDTO;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String message) {
	    ErrorResponseDTO response = new ErrorResponseDTO(
	        status.value(),
	        status.getReasonPhrase(),
	        message
	    );
	    return ResponseEntity.status(status).body(response);
	}

	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<ErrorResponseDTO> handleInsufficientBalanceException(InsufficientBalanceException ex) {
	    return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}
		
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException exception){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
				HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.getReasonPhrase(),
				exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
	}
	
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(OrderNotFoundException exception){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
				HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.getReasonPhrase(),
				exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
	}
	
	@ExceptionHandler(PositionNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(PositionNotFoundException exception){
		ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
				HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.getReasonPhrase(),
				exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
	}
	
		@ExceptionHandler(InvalidQuantityToBuyException.class)
		public ResponseEntity<ErrorResponseDTO> handleInvalidQuantityToBuyException(InvalidQuantityToBuyException exception){
			ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
					HttpStatus.BAD_REQUEST.value(),
					HttpStatus.BAD_REQUEST.getReasonPhrase(),
					exception.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
		}
		
		@ExceptionHandler(InsufficientQuantityToSellException.class)
		public ResponseEntity<ErrorResponseDTO> handleInsufficientQuantityToSellException(InsufficientQuantityToSellException exception){
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
		
		@ExceptionHandler(AssetNotFoundException.class)
		public ResponseEntity<ErrorResponseDTO> handleAssetSymbolNotFoundException(AssetNotFoundException exception){
			ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
					HttpStatus.NOT_FOUND.value(),
					HttpStatus.NOT_FOUND.getReasonPhrase(),
					exception.getMessage());
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
