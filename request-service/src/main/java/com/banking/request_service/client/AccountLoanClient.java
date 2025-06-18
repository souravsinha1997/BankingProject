package com.banking.request_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.banking.request_service.client.dto.Loan;



@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountLoanClient {

	@PostMapping("/api/loans")
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan);
	
}
