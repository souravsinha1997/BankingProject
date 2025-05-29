package com.banking.beneficiary_service.exception;

public class InvalidAccountNoException extends RuntimeException {
	public InvalidAccountNoException(String message) {
		super(message);
	}
}
