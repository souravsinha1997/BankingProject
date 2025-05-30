package com.banking.account_service.client.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatementTransaction {

	private String txnRefNo;
	private String accoutNo;
	private BigDecimal amount;
	private String bnfAccount;
	private String txnType;
	private LocalDateTime txnDate;
	
}
