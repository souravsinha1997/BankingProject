package com.banking.transaction_service.exception;

public class InvalidCifException extends RuntimeException {

	public InvalidCifException(String message) {
		super(message);
	}
}
