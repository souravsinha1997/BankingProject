package com.banking.request_service.exception;

public class InvalidCifException extends RuntimeException {
	public InvalidCifException(String message) {
		super(message);
	}
}
