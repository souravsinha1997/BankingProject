package com.banking.beneficiary_service.service;

import java.util.List;

import com.banking.beneficiary_service.dto.BeneficiaryRequest;
import com.banking.beneficiary_service.dto.BeneficiaryResponse;

public interface BeneficiaryService {
    List<BeneficiaryResponse> getAllBeneficiaries();
    
    BeneficiaryResponse getBeneficiary(Long id);
    
    BeneficiaryResponse addBeneficiary(BeneficiaryRequest beneficiaryRequest);
    
    BeneficiaryResponse updateBeneficiary(Long id, BeneficiaryRequest beneficiaryRequest);
    
    void deleteBeneficiary(Long id);
}
