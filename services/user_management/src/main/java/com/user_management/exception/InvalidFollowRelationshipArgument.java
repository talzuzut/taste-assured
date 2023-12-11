package com.user_management.exception;

public class InvalidFollowRelationshipArgument extends RuntimeException {
	public InvalidFollowRelationshipArgument() {
		super("Invalid follow relationship argument");
	}

	public InvalidFollowRelationshipArgument(String message) {
		super(message);
	}

}
