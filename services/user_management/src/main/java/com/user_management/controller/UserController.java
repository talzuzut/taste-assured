package com.user_management.controller;

import com.user_management.exception.ErrorMessageProvider;
import com.user_management.model.CreateUserDTO;
import com.user_management.model.User;
import com.user_management.service.api.UserService;
import com.user_management.service.impl.FollowRelationshipServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {
	private final UserService userService;
	private final FollowRelationshipServiceImpl followRelationshipService;

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
	public void addUser(@RequestBody @Valid CreateUserDTO createUserDTO) {
		User User = userService.convertUserDTOToUser(createUserDTO);
		log.info("User added: {}", User);
		userService.addUser(User);
	}


	@PostMapping("/demo_batch")
	@ResponseStatus(HttpStatus.CREATED)
	public void addBatchUsers() {
		log.info("inserting demo batch");
		userService.addBatchUsers();

	}


	@DeleteMapping("/{id}")
	public void deleteUserById(@PathVariable Long id) {
		if (!userService.userExistsById(id)) {
			String errorMessage = ErrorMessageProvider.getErrorMessage("error.not_found");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
		}
		log.info("Deleting User: {}", id);
		userService.deleteUserById(id);
		followRelationshipService.deleteFollowRelationshipsByUserId(id);
	}

}
