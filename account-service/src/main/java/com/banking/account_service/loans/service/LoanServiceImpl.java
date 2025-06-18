package com.banking.account_service.loans.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.account_service.client.TransactionClient;
import com.banking.account_service.loans.dto.TransactionRequest;
import com.banking.account_service.loans.dto.TransactionType;
import com.banking.account_service.loans.entity.Loan;
import com.banking.account_service.loans.repository.LoanRepository;

@Service
public class LoanServiceImpl implements LoanService {
    
    private final LoanRepository loanRepository;
    private final TransactionClient transactionClient;
    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository,TransactionClient transactionClient) {
        this.loanRepository = loanRepository;
        this.transactionClient = transactionClient;
    }
    
    @Override
    @Transactional
    public Loan createLoan(Loan loan) {
        
    	Loan savedLoan =  loanRepository.save(loan);
        
    	if(savedLoan.getStatus().equals("APPROVE")) {
    		//call transaction service
    		TransactionRequest transactionReq = new TransactionRequest();
    		transactionReq.setAccountNo(loan.getAccountNo());
    		transactionReq.setAmmount(loan.getAmount());
    		transactionReq.setCif(loan.getCif());
    		transactionReq.setTransactionType(TransactionType.LOAN);
    		transactionClient.completeTransactions(transactionReq);
    	}
    	
        return savedLoan;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Loan getLoanById(Integer id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Loan> getLoansByAccountNo(String accountNo) {
        return loanRepository.findByAccountNo(accountNo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Loan> getLoansByCif(String cif) {
        return loanRepository.findByCif(cif);
    }
}
