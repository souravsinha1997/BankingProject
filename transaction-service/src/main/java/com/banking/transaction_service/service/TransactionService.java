package com.banking.transaction_service.service;

import java.util.List;

import com.banking.transaction_service.client.dto.StatementRequest;
import com.banking.transaction_service.client.dto.StatementTransaction;
import com.banking.transaction_service.client.dto.Status;
import com.banking.transaction_service.dto.TransactionRequest;

public interface TransactionService {

	public String doTransaction(TransactionRequest request);
	public String doLoanTransaction(TransactionRequest request);
	public void updateTransaction(Status status);
	public List<StatementTransaction> getAccountStatement(String accountNo);
}
