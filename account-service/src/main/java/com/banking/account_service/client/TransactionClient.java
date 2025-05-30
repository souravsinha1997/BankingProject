package com.banking.account_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.banking.account_service.client.dto.StatementTransaction;


@FeignClient(name = "TRANSACTION-SERVICE")
public interface TransactionClient {

	@GetMapping("/api/transactions/statement/{accountNo}")
	public ResponseEntity<List<StatementTransaction>> getAccountStatement(@PathVariable String accountNo);
}
