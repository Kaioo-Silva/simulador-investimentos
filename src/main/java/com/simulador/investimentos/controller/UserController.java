package com.simulador.investimentos.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.simulador.investimentos.dtos.UserRequestDTO;
import com.simulador.investimentos.dtos.UserResponseDTO;
import com.simulador.investimentos.entity.User;
import com.simulador.investimentos.mappers.UserMapper;
import com.simulador.investimentos.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "UserController", description = "Operações relacionadas à manipulação de usuários")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final UserService userService;
	private final UserMapper userMapper;


    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;}

    @Operation(summary = "Criar um novo usuário", description = "Cria um novo usuário com saldo de R$1.000,00", responses = {
                @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
                @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
     })
    
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
    	UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
    	URI location = ServletUriComponentsBuilder
    	        .fromCurrentRequest()
    	        .path("/{id}")
    	        .buildAndExpand(userResponseDTO.id())
    	        .toUri();
        return ResponseEntity.created(location).body(userResponseDTO);
    }

    @Operation(summary = "Retorna todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados", responses = {
            @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    	List<UserResponseDTO> listOfAllUsers =userService.getAllUsers();
        return ResponseEntity.ok(listOfAllUsers);
    }
    
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário com base no ID fornecido", responses = {
            @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id){
    	User user =userService.findUser(id);
    	UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(user);
    	return ResponseEntity.ok(userResponseDTO);
    }
    
    @Operation(summary = "Excluir usuário por ID", description = "Exclui um usuário com base no ID fornecido", responses = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
    	userService.deleteUser(id);
    	return ResponseEntity.noContent().build();
    }

}


