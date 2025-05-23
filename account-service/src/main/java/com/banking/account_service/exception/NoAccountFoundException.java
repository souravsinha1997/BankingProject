package com.banking.account_service.exception;

public class NoAccountFoundException extends RuntimeException {

	public NoAccountFoundException(String message) {
		super(message);
	}
}
