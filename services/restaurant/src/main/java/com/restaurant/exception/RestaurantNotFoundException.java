package com.restaurant.exception;

import java.io.Serializable;

public class RestaurantNotFoundException extends RuntimeException implements Serializable {
	public RestaurantNotFoundException() {
		super(ErrorMessageProvider.getErrorMessage("error.not_found"));
	}

	public RestaurantNotFoundException(String message) {
		super(message);
	}
}
