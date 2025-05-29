package com.banking.transaction_service.exception;

public class UnableToUpdateTransaction extends RuntimeException {

	public UnableToUpdateTransaction(String message) {
		super(message);
	}
}
