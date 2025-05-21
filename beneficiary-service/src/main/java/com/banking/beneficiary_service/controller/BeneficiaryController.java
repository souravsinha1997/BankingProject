package com.banking.beneficiary_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.beneficiary_service.dto.BeneficiaryRequest;
import com.banking.beneficiary_service.dto.BeneficiaryResponse;
import com.banking.beneficiary_service.dto.MessageResponse;
import com.banking.beneficiary_service.service.BeneficiaryService;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService beneficiaryService;

    @GetMapping
    public ResponseEntity<List<BeneficiaryResponse>> getAllBeneficiaries() {
        List<BeneficiaryResponse> beneficiaries = beneficiaryService.getAllBeneficiaries();
        return ResponseEntity.ok(beneficiaries);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<BeneficiaryResponse> getBeneficiary(@PathVariable Long id) {
        BeneficiaryResponse beneficiary = beneficiaryService.getBeneficiary(id);
        return ResponseEntity.ok(beneficiary);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<MessageResponse> addBeneficiary(@RequestBody BeneficiaryRequest beneficiaryRequest) {
        BeneficiaryResponse savedBeneficiary = beneficiaryService.addBeneficiary(beneficiaryRequest);
        return ResponseEntity.ok(new MessageResponse("Beneficiary registered"));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<BeneficiaryResponse> updateBeneficiary(@PathVariable Long id, @RequestBody BeneficiaryRequest beneficiaryRequest) {
        BeneficiaryResponse updatedBeneficiary = beneficiaryService.updateBeneficiary(id, beneficiaryRequest);
        return ResponseEntity.ok(updatedBeneficiary);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteBeneficiary(@PathVariable Long id) {
        beneficiaryService.deleteBeneficiary(id);
        return ResponseEntity.ok(new MessageResponse("Beneficiary removed"));
    }
}

