package com.restaurant.exception;

import java.io.Serializable;

public class RestaurantExistsException extends RuntimeException implements Serializable {
	public RestaurantExistsException() {
		super(ErrorMessageProvider.getErrorMessage("error.exists"));
	}

	public RestaurantExistsException(String message) {
		super(message);
	}
}
