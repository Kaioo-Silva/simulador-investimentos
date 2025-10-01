package com.simulador.investimentos.unit.controller;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simulador.investimentos.config.BrapiConfig;
import com.simulador.investimentos.controller.AssetController;
import com.simulador.investimentos.dtos.AssetDTO;
import com.simulador.investimentos.dtos.QuoteResponseDTO;
import com.simulador.investimentos.exception.AssetNotFoundException;
import com.simulador.investimentos.service.AssetService;
import com.simulador.investimentos.service.factory.AssetDTOFactory;

import feign.FeignException;

@WebMvcTest(AssetController.class)
public class AssetControllerTest {
	

	    @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private  AssetService assetService;

	    @Autowired
	    private AssetController assetController;
	    
	    @Autowired
	    private ObjectMapper objectMapper;
	    
	    @Test
	    void should_Return_Asset_When_Symbol_Is_Valid() throws Exception  {
	        AssetDTO assetDTO = AssetDTOFactory.build(); 
	        QuoteResponseDTO responseDTO = new QuoteResponseDTO(List.of(assetDTO));
	        String symbol = assetDTO.symbol();

	        doReturn(responseDTO).when(assetService).getAssetData(symbol);

	        mockMvc.perform(get("/api/v1/assets/" + symbol))
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.results[0].symbol").value(symbol));
	    }
	    
	    @Test
	    void should_Return_404_When_Symbol_Is_Invalid() throws Exception {
	        String invalidSymbol = "INVALID123";

	        doThrow(new AssetNotFoundException())
	            .when(assetService).getAssetData(invalidSymbol);

	        mockMvc.perform(get("/api/v1/assets/" + invalidSymbol))
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isNotFound())
	            .andExpect(jsonPath("$.status").value(404))
	            .andExpect(jsonPath("$.error").value("Not Found"))
	            .andExpect(jsonPath("$.message").value("A ação informada não existe"));
	    }
	   
	   	
    }