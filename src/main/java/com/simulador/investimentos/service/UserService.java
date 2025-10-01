package com.simulador.investimentos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.simulador.investimentos.dtos.UserRequestDTO;
import com.simulador.investimentos.dtos.UserResponseDTO;
import com.simulador.investimentos.entity.User;
import com.simulador.investimentos.exception.UserNotFoundException;
import com.simulador.investimentos.mappers.UserMapper;
import com.simulador.investimentos.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public UserService(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
		User user = userMapper.toUserEntity(userRequestDTO);
		userRepository.save(user);
		UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(user);
		return userResponseDTO;

	}

	public List<UserResponseDTO> getAllUsers() {

		List<User> users = userRepository.findAll();
		List<UserResponseDTO> userResponseDTO = userMapper.toGetAllUserResponseDTO(users);
		return userResponseDTO;

	}

	public User findUser(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
		
	}
	
	public void deleteUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
		userRepository.delete(user);
	}

	

	
	

}
