package com.rating.exception;

public class RatingNotFoundException extends RuntimeException {
	public RatingNotFoundException(String message) {
		super(message);
	}

	public RatingNotFoundException() {
		super(ErrorMessageProvider.getErrorMessage("error.not_found"));
	}
}
