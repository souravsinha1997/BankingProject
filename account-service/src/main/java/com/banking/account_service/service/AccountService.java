package com.banking.account_service.service;

import java.util.List;

import com.banking.account_service.client.dto.AccountRequest;
import com.banking.account_service.client.dto.AccountResponse;
import com.banking.account_service.client.dto.BankTransferRequest;
import com.banking.account_service.client.dto.TransactionRequest;

public interface AccountService {
	 	AccountResponse createAccount(AccountRequest accountRequest);
	    List<AccountResponse> getAllAccounts();
	    AccountResponse getAccountByAccountNo(String accountNo);
	    AccountResponse updateAccount(AccountRequest accountDetails);
	    String closeAccount(String accountNo);
	    AccountResponse withdrawOrDepositFunds(TransactionRequest request);
	    void transferFunds(BankTransferRequest request);
	    boolean validateAccount(String accountNo);
	    String getCIFByAccountNo(String accountNo);
}
