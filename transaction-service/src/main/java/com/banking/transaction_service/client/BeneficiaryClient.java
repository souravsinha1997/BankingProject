package com.banking.transaction_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.banking.transaction_service.client.dto.BeneficiaryRequest;
import com.banking.transaction_service.client.dto.BeneficiaryResponse;
import com.banking.transaction_service.client.dto.MessageResponse;



@FeignClient(name = "BENEFICIARY-SERVICE")
public interface BeneficiaryClient {

	@GetMapping("/api/beneficiaries/{id}")
    public ResponseEntity<BeneficiaryResponse> getBeneficiary(@PathVariable Long id);
	
	@PostMapping("/api/beneficiaries")
    public ResponseEntity<MessageResponse> addBeneficiary(@RequestBody BeneficiaryRequest beneficiaryRequest);
}
