package com.banking.account_service.dto;

import java.math.BigDecimal;
import java.util.List;

import com.banking.account_service.client.dto.StatementTransaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatement {

	private String accountNo;
	private String cif;
	private String accountType;
	private BigDecimal balance;
	private String currency;
	private String status;
	private List<StatementTransaction> transactions;
	
}
