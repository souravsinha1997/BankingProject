package com.banking.account_service.loans.repository;

import com.banking.account_service.loans.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    List<Loan> findByCif(String cif);
    List<Loan> findByAccountNo(String accountNo);
    List<Loan> findByStatus(String status);
    List<Loan> findByAmountGreaterThanEqual(BigDecimal amount);
    List<Loan> findByStartDateGreaterThanEqual(java.time.LocalDateTime startDate);
}
