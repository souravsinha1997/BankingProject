package com.banking.account_service.exception;

public class UnableToCompleteException extends RuntimeException {

	public UnableToCompleteException(String message) {
		super(message);
	}
	
	public UnableToCompleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
