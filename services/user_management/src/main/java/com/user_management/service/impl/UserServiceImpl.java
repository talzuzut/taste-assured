package com.user_management.service.impl;

import com.user_management.model.CreateUserDTO;
import com.user_management.model.User;
import com.user_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements com.user_management.service.api.UserService {
private final UserRepository userRepository;

	@Override
	public User convertUserDTOToUser(CreateUserDTO createUserDTO) {
		return User.builder()
				.username(createUserDTO.getUsername())
				.password(createUserDTO.getPassword())
				.firstname(createUserDTO.getFirstname())
				.lastname(createUserDTO.getLastname())
				.email(createUserDTO.getEmail())
				.createdAt(LocalDateTime.now())
				.build();
	}

	@Override
	public boolean addUser(User user) {
		try {
			userRepository.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	@Override
	public boolean userExistsById(Long id) {
		return userRepository.existsById(id);
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public void addBatchUsers() {
		LinkedList<User> users = new LinkedList<>();
		for (int i = 1; i <= 100; i++) {
			users.add(User.builder()
					.username("user" + i)
					.password("password" + i)
					.firstname("firstname" + i)
					.lastname("lastname" + i)
					.email("email" + i)
					.createdAt(LocalDateTime.now())
					.build());
		}
		userRepository.saveAll(users);
	}
}
