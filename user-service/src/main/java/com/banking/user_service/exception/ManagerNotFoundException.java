package com.banking.user_service.exception;

public class ManagerNotFoundException extends RuntimeException {

	public ManagerNotFoundException(String message) {
		super(message);
	}
}
