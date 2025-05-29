package com.banking.account_service.client.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankTransferRequest {

	private String fromAccount;
	private BigDecimal ammount;
	private String toAccount;
	private String txnRefNo;
}
