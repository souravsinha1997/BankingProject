package com.banking.account_service.client.dto;

import java.math.BigDecimal;

import com.banking.account_service.entity.AccountType;

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
public class AccountRequest {
	private String accountNumber;  // Optional, will be generated if not provided
    private String cif;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
}
