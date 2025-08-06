package com.simulador.investimentos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.investimentos.entity.Users;
import com.simulador.investimentos.repository.UsersRepository;
import com.simulador.investimentos.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final UserService userService;

    public UserController(UserService repository) {
        this.userService = repository;
    }

    @PostMapping
    public Users createUser(@RequestBody Users user) {
        return userService.criarUsuario(user);
    }

    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAll();
    }
}


