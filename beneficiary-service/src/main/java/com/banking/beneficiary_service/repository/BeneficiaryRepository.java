package com.banking.beneficiary_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.beneficiary_service.entity.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long>{
    List<Beneficiary> findByUserId(Long userId);
    
    Optional<Beneficiary> findByAccountNoAndUserId(String accountNo, Long userId);
    
    boolean existsByAccountNoAndUserId(String accountNo, Long userId);
}
