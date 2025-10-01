package com.simulador.investimentos.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simulador.investimentos.dtos.UserRequestDTO;
import com.simulador.investimentos.dtos.UserResponseDTO;
import com.simulador.investimentos.entity.User;
import com.simulador.investimentos.exception.UserNotFoundException;
import com.simulador.investimentos.mappers.UserMapper;
import com.simulador.investimentos.repository.UserRepository;
import com.simulador.investimentos.service.UserService;
import com.simulador.investimentos.service.factory.UserFactory;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private UserMapper userMapper;

	@InjectMocks
	private UserService userService;

	@Captor
	private ArgumentCaptor<User> userArgumentCaptor;

	@Nested
	class createUser {

		@Test
		void should_Create_User() {
			User user = UserFactory.build();
			UserRequestDTO userRequestDTO = new UserRequestDTO("John");
			UserResponseDTO userResponseDTO = new UserResponseDTO(1L, "John", 2L);

			doReturn(user).when(userMapper).toUserEntity(userRequestDTO);
			doReturn(user).when(userRepository).save(user);
			doReturn(userResponseDTO).when(userMapper).toUserResponseDTO(user);

			UserResponseDTO result = userService.createUser(userRequestDTO);

			assertThat(result).isEqualTo(userResponseDTO);

			verify(userMapper).toUserEntity(userRequestDTO);
			verify(userRepository).save(user);
			verify(userMapper).toUserResponseDTO(user);
		}
	}

	@Nested
	class getAllUsers {
		@Test
		void should_Get_All_Users() {
			List<User> users = List.of(new User(), new User());
			List<UserResponseDTO> expectedDTOs = List.of(new UserResponseDTO(1L, "John", 10L),
					new UserResponseDTO(2L, "Maria", 11L));

			doReturn(users).when(userRepository).findAll();
			doReturn(expectedDTOs).when(userMapper).toGetAllUserResponseDTO(users);

			List<UserResponseDTO> result = userService.getAllUsers();

			verify(userRepository).findAll();
			verify(userMapper).toGetAllUserResponseDTO(users);

			assertThat(result).isNotNull();
			assertThat(result).hasSize(2);
			assertThat(result).isEqualTo(expectedDTOs);
		}
	}

	@Nested
	class findUser {
		@Test
		void should_Find_User_When_Id_Exists() {
			Long id = 1L;
			User user = new User();
			doReturn(Optional.of(user)).when(userRepository).findById(id);

			User result = userService.findUser(id);

			verify(userRepository).findById(id);
			assertThat(result).isEqualTo(user);
		}

		@Test
		void should_Throw_Exception_When_User_Id_Not_Found() {
			Long nonExistingId = 999L;
			doReturn(Optional.empty()).when(userRepository).findById(nonExistingId);

			assertThrows(UserNotFoundException.class, () -> {
				userService.findUser(nonExistingId);
			});

			verify(userRepository).findById(nonExistingId);
		}

	}
	
	@Nested
	class deleteUser {
		@Test
		void should_Delete_User_When_Id_Exists() {
		    Long id = 1L;
		    User user = new User();
		    doReturn(Optional.of(user)).when(userRepository).findById(id);

		    userService.deleteUser(id);

		    verify(userRepository).findById(id);
		    verify(userRepository).delete(user);
		}

		@Test
		void should_Throw_Exception_When_User_Id_Not_Found_On_Delete() {
		    Long nonExistingId = 999L;
		    doReturn(Optional.empty()).when(userRepository).findById(nonExistingId);

		    assertThrows(UserNotFoundException.class, () -> {
		        userService.deleteUser(nonExistingId);
		    });

		    verify(userRepository).findById(nonExistingId);
		    verifyNoMoreInteractions(userRepository);
		}
	}
}
