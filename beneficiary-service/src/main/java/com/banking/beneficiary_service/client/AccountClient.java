package com.banking.beneficiary_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountClient {

	@GetMapping("/api/accounts/validate/{accountNo}")
    public ResponseEntity<Boolean> validateAccount(@PathVariable String accountNo);
	
}
