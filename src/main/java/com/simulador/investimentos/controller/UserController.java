package com.simulador.investimentos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.investimentos.dtos.UserRequestDTO;
import com.simulador.investimentos.dtos.UserResponseDTO;
import com.simulador.investimentos.entity.User;
import com.simulador.investimentos.mappers.UserMapper;
import com.simulador.investimentos.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;}


    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
    	UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    	List<UserResponseDTO> listOfAllUsers =userService.getAllUsers();
        return ResponseEntity.ok(listOfAllUsers);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUser(@PathVariable Long id){
    	User user =userService.findUser(id);
    	UserResponseDTO userResponseDTO = UserMapper.toUserResponseDTO(user);
    	return ResponseEntity.ok(userResponseDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
    	userService.deleteUser(id);
    	return ResponseEntity.noContent().build();
    }

}


