package com.banking.transaction_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.banking.transaction_service.client.dto.AccountResponse;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountClient {

	@GetMapping("/api/accounts/cif/{accountNo}")
    public ResponseEntity<String> getAccountCIF(@PathVariable String accountNo);
}
