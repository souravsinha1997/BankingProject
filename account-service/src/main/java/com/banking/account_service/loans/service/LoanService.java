package com.banking.account_service.loans.service;

import com.banking.account_service.loans.entity.Loan;
import java.util.List;

public interface LoanService {
    
    Loan createLoan(Loan loan);
    
    Loan getLoanById(Integer id);
    
    List<Loan> getLoansByAccountNo(String accountNo);
    
    List<Loan> getLoansByCif(String cif);
}
