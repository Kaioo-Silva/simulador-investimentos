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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simulador.investimentos.controller.PositionController;
import com.simulador.investimentos.dtos.PositionDTO;
import com.simulador.investimentos.entity.Position;
import com.simulador.investimentos.exception.PositionNotFoundException;
import com.simulador.investimentos.mappers.PositionMapper;
import com.simulador.investimentos.service.PositionService;
import com.simulador.investimentos.service.factory.PositionDTOFactory;
import com.simulador.investimentos.service.factory.PositionFactory;

@WebMvcTest(PositionController.class)
public class PositionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PositionService positionService;

	@Autowired
	private PositionController positionController;
	
	@MockBean
	private PositionMapper positionMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Nested
	class getPosition {

		@Test
		void should_Get_Position_When_Everything_Is_Ok() throws Exception {
			Position position = PositionFactory.build();
			PositionDTO positionDTO = PositionDTOFactory.build();
			Long walletId = 2L;
			String assetSymbol = "PETR4";
			
			doReturn(position).when(positionService).findPosition(walletId, assetSymbol);
			doReturn(positionDTO).when(positionMapper).toPositionDTO(position);
			
			mockMvc.perform(get("/api/v1/positions/wallets/" + walletId)
		    	.param("assetSymbol", assetSymbol))
			    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
	            .andExpect(jsonPath("$.assetSymbol").value(positionDTO.assetSymbol()))
	            .andExpect(jsonPath("$.totalQuantity").value(positionDTO.totalQuantity()))
	            .andExpect(jsonPath("$.averagePrice").value(positionDTO.averagePrice().toString()));

									
		}
	
		@Test
		void should_Return_404_When_Position_Not_Found() throws Exception {
		    Long walletId = 2L;
		    String assetSymbol = "INVALID4";

		    doThrow(new PositionNotFoundException())
		        .when(positionService).findPosition(walletId, assetSymbol);

		    mockMvc.perform(get("/api/v1/positions/wallets/" + walletId)
		        .param("assetSymbol", assetSymbol))
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isNotFound())
		        .andExpect(jsonPath("$.message").value("Posição não encontrada"));
		}

		@Test
		void should_Return_All_Positions_For_Wallet_When_Wallet_Exists() throws Exception {
		    Long walletId = 1L;
		    List<PositionDTO> expectedPositions = List.of(
		        PositionDTOFactory.build(),
		        PositionDTOFactory.build());

		    doReturn(expectedPositions).when(positionService).getAllPositions(walletId);

		    mockMvc.perform(get("/api/v1/positions/allpositions/wallets/" + walletId))
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.length()").value(expectedPositions.size()))
		        .andExpect(jsonPath("$[0].assetSymbol").value(expectedPositions.get(0).assetSymbol()))
		        .andExpect(jsonPath("$[0].totalQuantity").value(expectedPositions.get(0).totalQuantity()))
		        .andExpect(jsonPath("$[1].assetSymbol").value(expectedPositions.get(1).assetSymbol()))
		        .andExpect(jsonPath("$[1].totalQuantity").value(expectedPositions.get(1).totalQuantity()));
		}
		
		@Test
		void should_Return_404_When_No_Positions_Found_For_Wallet() throws Exception {
		    Long walletId = 1L;

		    doThrow(new PositionNotFoundException())
		        .when(positionService).getAllPositions(walletId);

		    mockMvc.perform(get("/api/v1/positions/allpositions/wallets/" + walletId))
		        .andExpect(status().isNotFound())
		        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		        .andExpect(jsonPath("$.message").value("Posição não encontrada"));
		}


	}
}
			

			   