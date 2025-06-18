package com.banking.account_service.loans.controller;

import com.banking.account_service.loans.entity.Loan;
import com.banking.account_service.loans.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    
    private final LoanService loanService;
    
    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }
    
    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        return new ResponseEntity<>(loanService.createLoan(loan), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Integer id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }
    
    @GetMapping("/account/{accountNo}")
    public ResponseEntity<List<Loan>> getLoansByAccountNo(@PathVariable String accountNo) {
        return ResponseEntity.ok(loanService.getLoansByAccountNo(accountNo));
    }
    
    @GetMapping("/cif/{cif}")
    public ResponseEntity<List<Loan>> getLoansByCif(@PathVariable String cif) {
        return ResponseEntity.ok(loanService.getLoansByCif(cif));
    }
}
