package com.user_management.service;

import com.user_management.model.CreateUserDTO;
import com.user_management.model.User;
import com.user_management.repository.UserRepository;
import com.user_management.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private  UserRepository userRepository;

	private final static String VALID_USERNAME = "ValidUser";
	private final static String VALID_PASSWORD = "ValidPassword";
	private final static String VALID_FIRSTNAME = "John";
	private final static String VALID_LASTNAME = "Doe";
	private final static String VALID_EMAIL = "johndoe@example.com";



	@Test
	public void givenUserService_whenConvertUserDTOToUser_thenSuccess() {
		CreateUserDTO createUserDTO = CreateUserDTO.builder()
				.username(VALID_USERNAME)
				.password(VALID_PASSWORD)
				.firstname(VALID_FIRSTNAME)
				.lastname(VALID_LASTNAME)
				.email(VALID_EMAIL)
				.build();

		User user = userService.convertUserDTOToUser(createUserDTO);

		assertNotNull(user);
		assertEquals(VALID_USERNAME, user.getUsername());
		assertEquals(VALID_PASSWORD, user.getPassword());
		assertEquals(VALID_FIRSTNAME, user.getFirstname());
		assertEquals(VALID_LASTNAME, user.getLastname());
		assertEquals(VALID_EMAIL, user.getEmail());
	}

	@Test
	public void givenUserService_whenAddUser_thenSuccess() {
		User user = User.builder()
				.username(VALID_USERNAME)
				.password(VALID_PASSWORD)
				.firstname(VALID_FIRSTNAME)
				.lastname(VALID_LASTNAME)
				.email(VALID_EMAIL)
				.build();
		assert(userService.addUser(user));
		Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
	}

	@Test
	public void givenUserService_whenUserExistsById_thenSuccess() {
		long userId = 1L;
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);

		boolean userExists = userService.userExistsById(userId);

		assertTrue(userExists);
	}

	@Test
	public void givenUserService_whenUserDoesNotExistById_thenSuccess() {
		long userId = 1L;
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);

		boolean userExists = userService.userExistsById(userId);

		assertFalse(userExists);
	}

	@Test
	public void givenUserService_whenGetUserById_thenSuccess() {
		long userId = 1L;
		User user = User.builder()
				.username(VALID_USERNAME)
				.password(VALID_PASSWORD)
				.firstname(VALID_FIRSTNAME)
				.lastname(VALID_LASTNAME)
				.email(VALID_EMAIL)
				.build();

		Mockito.when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

		User foundUser = userService.getUserById(userId);

		assertNotNull(foundUser);
		assertSame(user, foundUser);
	}

	@Test
	public void givenUserService_whenGetUserByIdNotFound_thenSuccess() {
		long userId = 1L;
		Mockito.when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

		User foundUser = userService.getUserById(userId);

		assertNull(foundUser);
	}

	@Test
	public void givenUserService_whenDeleteUserById_thenSuccess() {
		long userId = 1L;
		Mockito.doNothing().when(userRepository).deleteById(userId);

		userService.deleteUserById(userId);
		Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
	}
}
