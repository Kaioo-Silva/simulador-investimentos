package com.simulador.investimentos.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simulador.investimentos.controller.UserController;
import com.simulador.investimentos.dtos.UserRequestDTO;
import com.simulador.investimentos.dtos.UserResponseDTO;
import com.simulador.investimentos.mappers.UserMapper;
import com.simulador.investimentos.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {


	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;
	
	@MockBean
	private UserMapper userMapper;
	
	@Autowired
	private UserController userController;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Nested
	class createUser {
	
	@Test
	void should_Create_User_When_Valid_Request() throws Exception {
	    UserRequestDTO userRequestDTO = new UserRequestDTO("John"); 
	    UserResponseDTO userResponseDTO = new UserResponseDTO(1L, "John", 2L);  
	    String jsonRequest = objectMapper.writeValueAsString(userRequestDTO);

	    doReturn(userResponseDTO).when(userService).createUser(userRequestDTO);

	   mockMvc.perform(post("/api/v1/users")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(jsonRequest))
	        .andExpect(status().isCreated())
            .andExpect(header().string("Location", "http://localhost/api/v1/users/1"))
	        .andExpect(jsonPath("$.id").value(userResponseDTO.id()))
	        .andExpect(jsonPath("$.name").value(userResponseDTO.name()));	;
	 

	}
	}
	
	  
	


}
