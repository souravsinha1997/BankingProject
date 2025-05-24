package com.banking.account_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.banking.account_service.client.dto.AccountRequest;
import com.banking.account_service.client.dto.AccountResponse;
import com.banking.account_service.client.dto.TransactionRequest;
import com.banking.account_service.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest) {
        AccountResponse response = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{accountNo}")
    public ResponseEntity<AccountResponse> getAccountByAccountNo(@PathVariable String accountNo) {
        AccountResponse response = accountService.getAccountByAccountNo(accountNo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<AccountResponse> updateAccount(@RequestBody AccountRequest accountDetails) {
        AccountResponse response = accountService.updateAccount(accountDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{accountNo}")
    public ResponseEntity<String> closeAccount(@PathVariable String accountNo) {
        String response = accountService.closeAccount(accountNo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/transactions")
    public ResponseEntity<AccountResponse> creditOrDebitAccount(@RequestBody TransactionRequest request) {
        AccountResponse response = accountService.creditOrDebitAccount(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
