package com.banking.account_service.loans.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

	private String cif;
	private String accountNo;
	private BigDecimal ammount;
	private TransactionType transactionType;
	private int bnf_id;
	private String bnf_name;
	private String bnf_fullName;
	private String bnf_accountNo;
	private String bnf_ifsc;
	private String bnf_bank;
}
