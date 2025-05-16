package com.banking.user_service.exception;

public class InvalidLoginException extends RuntimeException{

	public InvalidLoginException(String message) {
		super(message);
	}
}
