package com.user_management.service;

import com.user_management.model.User;
import com.user_management.model.CreateUserDTO;
import com.user_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
private final UserRepository userRepository;
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

	public boolean addUser(User user) {
		try {
			userRepository.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	public boolean userExistsById(Long id) {
		return userRepository.existsById(id);
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}
}
