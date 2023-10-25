package com.restaurant.exception;

import com.google.maps.errors.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler({RestaurantExistsException.class, RestaurantNotFoundException.class})
	public ResponseEntity<Object> handleRestaurantExistsException(Exception ex) {
		log.error(ex.getMessage());
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler({IOException.class, InterruptedException.class, ApiException.class})
	public ResponseEntity<Object> handleGoogleMapsException(Exception ex) {
		log.error(ex.getMessage());
		return ResponseEntity.badRequest().body("Error while fetching restaurant from Google Maps");
	}


	/*@ExceptionHandler(MethodArgumentNotValidException.class)
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

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}*/


}


