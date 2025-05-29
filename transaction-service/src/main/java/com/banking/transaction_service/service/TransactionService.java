package com.banking.transaction_service.service;

import java.util.List;

import com.banking.transaction_service.client.dto.Status;
import com.banking.transaction_service.dto.TransactionRequest;
import com.banking.transaction_service.entity.Transactions;

public interface TransactionService {

	public String doTransaction(TransactionRequest request);
	public void updateTransaction(Status status);
}
