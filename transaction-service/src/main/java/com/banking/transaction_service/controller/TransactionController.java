package com.banking.transaction_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.transaction_service.client.dto.StatementTransaction;
import com.banking.transaction_service.dto.TransactionRequest;
import com.banking.transaction_service.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@PostMapping
	public ResponseEntity<String> completeTransactions(@RequestBody TransactionRequest request){
		return ResponseEntity.ok(transactionService.doTransaction(request));
	}
	
	@GetMapping("/statement/{accountNo}")
	public ResponseEntity<List<StatementTransaction>> getAccountStatement(@PathVariable String accountNo){
		return ResponseEntity.ok(transactionService.getAccountStatement(accountNo));
	}
	
}
