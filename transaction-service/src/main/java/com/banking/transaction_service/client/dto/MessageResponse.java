package com.banking.transaction_service.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

	String message;
	
	public MessageResponse(String message) {
		this.message = message;
	}
}
