package com.banking.transaction_service.client.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankTransactionRequest {

	private String accountNo;
	private BigDecimal amount;
	private String transaction; // credit or debit
	private String txnRefNo;
	
}
