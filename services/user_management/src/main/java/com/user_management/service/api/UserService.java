package com.user_management.service.api;

import com.user_management.model.CreateUserDTO;
import com.user_management.model.User;

public interface UserService {
	User convertUserDTOToUser(CreateUserDTO createUserDTO);

	boolean addUser(User user);

	boolean userExistsById(Long id);

	User getUserById(Long id);

	void deleteUserById(Long id);

	void addBatchUsers();
}
