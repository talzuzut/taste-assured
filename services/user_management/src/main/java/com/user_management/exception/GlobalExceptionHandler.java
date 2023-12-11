package com.user_management.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<String> errorMessages = new ArrayList<>();
		for (FieldError fieldError : fieldErrors) {
			errorMessages.add(fieldError.getDefaultMessage());
		}

		return ResponseEntity.badRequest().body("Invalid input- \n" + errorMessages);
	}


	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
		return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

		if (ex.getCause() != null && ex.getCause().getCause() instanceof DateTimeParseException)
			return ResponseEntity.badRequest().body("Invalid input- \n" + "Date must be in format yyyy-MM-dd");
		else return ResponseEntity.badRequest().body("Please provide a valid input");
	}

	@ExceptionHandler({IllegalArgumentException.class, InvalidFollowRelationshipArgument.class})
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		return ResponseEntity.badRequest().body("Invalid input- \n" + "such a record already exists");
	}

}


