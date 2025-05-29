package com.banking.transaction_service.client.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
	 	private String accountNumber;
	    private String cif;
	    private AccountType accountType;
	    private BigDecimal balance;
	    private String currency;
	    private AccountStatus status;
}
