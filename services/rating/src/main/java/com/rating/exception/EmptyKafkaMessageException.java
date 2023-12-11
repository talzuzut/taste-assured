package com.rating.exception;

public class EmptyKafkaMessageException extends RuntimeException {
	public EmptyKafkaMessageException(String message) {
		super(message);
	}

	public EmptyKafkaMessageException() {
		super("Empty kafka message");
	}
}
