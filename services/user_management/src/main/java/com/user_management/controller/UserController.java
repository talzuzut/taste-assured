package com.user_management.controller;

import com.user_management.exception.ErrorMessageProvider;
import com.user_management.model.User;
import com.user_management.model.CreateUserDTO;
import com.user_management.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {

		log.info("User requested: {}", id);
		User UserById = userService.getUserById(id);
		if (UserById == null) {
			String errorMessage = ErrorMessageProvider.getErrorMessage("error.not_found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
		}
		return ResponseEntity.ok(UserById);

	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addUser(@RequestBody @Valid CreateUserDTO CreateUserDTO) {
		User User = userService.convertUserDTOToUser(CreateUserDTO);
		log.info("User added: {}", User);
		userService.addUser(User);
	}



	@DeleteMapping("/{id}")
	public void deleteUserById(@PathVariable Long id) {
		if (!userService.userExistsById(id)) {
			String errorMessage = ErrorMessageProvider.getErrorMessage("error.not_found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
		}
		log.info("Deleting User: {}", id);
		userService.deleteUserById(id);
	}

}
