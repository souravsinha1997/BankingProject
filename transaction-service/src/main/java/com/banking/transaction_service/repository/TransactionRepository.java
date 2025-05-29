package com.banking.transaction_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.transaction_service.entity.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions, Long>{

	List<Transactions> findByReferenceNo(String referenceNo);
}
