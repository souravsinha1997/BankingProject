package com.banking.beneficiary_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.banking.beneficiary_service.dto.BeneficiaryRequest;
import com.banking.beneficiary_service.dto.BeneficiaryResponse;
import com.banking.beneficiary_service.entity.Beneficiary;
import com.banking.beneficiary_service.exception.BeneficiaryNotFoundException;
import com.banking.beneficiary_service.repository.BeneficiaryRepository;
import com.banking.beneficiary_service.security.JwtService;

@Service
public class BeneficiaryServiceImpl implements BeneficiaryService{
	
	@Autowired
    private BeneficiaryRepository beneficiaryRepository;

	@Autowired
	private JwtService jwtService;
	
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getCredentials().toString();
        return Long.valueOf(jwtService.getCustomerId(token));
    }

    private BeneficiaryResponse convertToResponse(Beneficiary beneficiary) {
        BeneficiaryResponse response = new BeneficiaryResponse();
        //response.setId(beneficiary.getId());
        response.setName(beneficiary.getName());
        response.setFullName(beneficiary.getFullName());
        response.setBankName(beneficiary.getBankName());
        response.setAccountNo(beneficiary.getAccountNo());
        response.setIfscCode(beneficiary.getIfscCode());
        return response;
    }

    private Beneficiary convertToEntity(BeneficiaryRequest request) {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setName(request.getName());
        beneficiary.setFullName(request.getFullName());
        beneficiary.setBankName(request.getBankName());
        beneficiary.setAccountNo(request.getAccountNo());
        beneficiary.setIfscCode(request.getIfscCode());
        return beneficiary;
    }

    @Override
    public List<BeneficiaryResponse> getAllBeneficiaries() {
        Long userId = getCurrentUserId();
        List<Beneficiary> beneficiaries = beneficiaryRepository.findByUserId(userId);
        return beneficiaries.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BeneficiaryResponse getBeneficiary(Long id) {
        Long userId = getCurrentUserId();
        Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(id);
        if (beneficiary.isPresent() && beneficiary.get().getUserId().equals(userId)) {
            return convertToResponse(beneficiary.get());
        }
        throw new BeneficiaryNotFoundException("Beneficiary not found with id: " + id);
    }

    @Override
    public BeneficiaryResponse addBeneficiary(BeneficiaryRequest beneficiaryRequest) {
        Long userId = getCurrentUserId();
        Beneficiary beneficiary = convertToEntity(beneficiaryRequest);
        beneficiary.setUserId(userId);
        
        // Check if beneficiary with same account number already exists
        if (beneficiaryRepository.existsByAccountNoAndUserId(beneficiary.getAccountNo(), userId)) {
            throw new BeneficiaryNotFoundException("Beneficiary with this account number already exists");
        }
        
        return convertToResponse(beneficiaryRepository.save(beneficiary));
    }

    @Override
    public BeneficiaryResponse updateBeneficiary(Long id, BeneficiaryRequest beneficiaryRequest) {
        Long userId = getCurrentUserId();
        Optional<Beneficiary> existingBeneficiary = beneficiaryRepository.findById(id);
        
        if (existingBeneficiary.isPresent() && existingBeneficiary.get().getUserId().equals(userId)) {
            Beneficiary beneficiary = existingBeneficiary.get();
            
            // Update only if the field is not null and not empty
            if (beneficiaryRequest.getName() != null && !beneficiaryRequest.getName().trim().isEmpty()) {
                beneficiary.setName(beneficiaryRequest.getName());
            }
            if (beneficiaryRequest.getFullName() != null && !beneficiaryRequest.getFullName().trim().isEmpty()) {
                beneficiary.setFullName(beneficiaryRequest.getFullName());
            }
            if (beneficiaryRequest.getBankName() != null && !beneficiaryRequest.getBankName().trim().isEmpty()) {
                beneficiary.setBankName(beneficiaryRequest.getBankName());
            }
            if (beneficiaryRequest.getAccountNo() != null && !beneficiaryRequest.getAccountNo().trim().isEmpty()) {
                beneficiary.setAccountNo(beneficiaryRequest.getAccountNo());
            }
            if (beneficiaryRequest.getIfscCode() != null && !beneficiaryRequest.getIfscCode().trim().isEmpty()) {
                beneficiary.setIfscCode(beneficiaryRequest.getIfscCode());
            }
            
            return convertToResponse(beneficiaryRepository.save(beneficiary));
        }
        throw new BeneficiaryNotFoundException("Beneficiary not found with id: " + id);
    }

    @Override
    public void deleteBeneficiary(Long id) {
        Long userId = getCurrentUserId();
        Optional<Beneficiary> existingBeneficiary = beneficiaryRepository.findById(id);
        
        if (existingBeneficiary.isPresent() && existingBeneficiary.get().getUserId().equals(userId)) {
            beneficiaryRepository.deleteById(id);
        } else {
            throw new BeneficiaryNotFoundException("Beneficiary not found with id: " + id);
        }
    }
}
