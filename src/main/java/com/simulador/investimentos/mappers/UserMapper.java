package com.simulador.investimentos.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.simulador.investimentos.dtos.UserRequestDTO;
import com.simulador.investimentos.dtos.UserResponseDTO;
import com.simulador.investimentos.entity.User;

@Component
public class UserMapper {

	public User toUserEntity(UserRequestDTO userRequestDTO) {
		if (userRequestDTO == null) {
			return null;
		}
		User user = new User(userRequestDTO.name(), null);
		return user;
	}

	public UserResponseDTO toUserResponseDTO(User user) {

		Long userId = user.getId();
		String userName = user.getName();
		Long userWalletId = user.getWallet().getId();

		UserResponseDTO userResponseDTO = new UserResponseDTO(userId, userName, userWalletId);
		return userResponseDTO;
	}

	public List<UserResponseDTO> toGetAllUserResponseDTO(List<User> users) {
		List<UserResponseDTO> userResponseDTO = users.stream().map(this::toUserResponseDTO)
				.collect(Collectors.toList());
		return userResponseDTO;
	}

}
