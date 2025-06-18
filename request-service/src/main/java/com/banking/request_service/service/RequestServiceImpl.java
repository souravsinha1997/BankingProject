package com.banking.request_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.banking.request_service.client.AccountLoanClient;
import com.banking.request_service.client.UserClient;
import com.banking.request_service.client.dto.Loan;
import com.banking.request_service.client.dto.UserResponse;
import com.banking.request_service.entity.Request;
import com.banking.request_service.exception.InvalidCifException;
import com.banking.request_service.repository.RequestRepository;
import com.banking.request_service.security.JwtService;

import jakarta.transaction.Transactional;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserClient userClient;
    private final JwtService jwtService;
    private final AccountLoanClient loanClient;
    
    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository,UserClient userClient,JwtService jwtService,AccountLoanClient loanClient) {
        this.requestRepository = requestRepository;
        this.userClient = userClient;
        this.jwtService = jwtService;
        this.loanClient = loanClient;
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getCredentials().toString();
        return Long.valueOf(jwtService.getCustomerId(token));
    }

    private String getCurrentCif() {
        int userId = getCurrentUserId().intValue();
        UserResponse user = userClient.getUser(userId).getBody();
        return user.getCif();
    }

    private void validateCif(String actualCif) {
        String currentCif = getCurrentCif();
        if (!actualCif.equals(currentCif)) {
            throw new InvalidCifException("Invalid CIF for the account");
        }
    }

    @Override
    @Transactional
    public String createRequest(Request request) {
        // Timestamps (initiationDate, updatedDate) are handled by @PrePersist in Request entity
    	Integer managerId = userClient.getManagerId(request.getCif()).getBody();
    	request.setApprover(managerId);
        if (request.getStatus() == null || request.getStatus().isEmpty()) {
            request.setStatus("PENDING"); // Default status
        }
        requestRepository.save(request);
        
        return "Request initiated successfully";
    }

    @Override
    @Transactional
    public Optional<Request> getRequestById(Integer id) {
        return requestRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    @Transactional
    public Request updateRequest(Integer id, Request requestDetails) {
        Request existingRequest = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));

        // Update fields from requestDetails if they are provided
        if (requestDetails.getCif() != null) {
            existingRequest.setCif(requestDetails.getCif());
        }
        if (requestDetails.getAccountNo() != null) {
            existingRequest.setAccountNo(requestDetails.getAccountNo());
        }
        if (requestDetails.getType() != null) {
            existingRequest.setType(requestDetails.getType());
        }
        if (requestDetails.getStatus() != null) {
            existingRequest.setStatus(requestDetails.getStatus());
        }
        // updatedDate is handled by @PreUpdate in Request entity
        Loan loan = new Loan();
        loan.setAccountNo(existingRequest.getAccountNo());
        String[] loantype = existingRequest.getType().toString().split("_");
        if(loantype[0].equals("LOAN")) loan.setAmount(new BigDecimal(loantype[1]));
        else throw new RuntimeException("Invalid Loan type");
        loan.setCif(existingRequest.getCif());
        loan.setReqId(existingRequest.getId());
        loan.setStartDate(LocalDateTime.now());
        loan.setStatus(requestDetails.getStatus());
        
        loanClient.createLoan(loan);
        
        return requestRepository.save(existingRequest);
    }

    @Override
    @Transactional
    public List<Request> findByCif() {
    	String cif = getCurrentCif();
        return requestRepository.findByCif(cif);
    }

    @Override
    @Transactional
    public List<Request> findByAccountNo(String accountNo) {
        return requestRepository.findByAccountNo(accountNo);
    }

    @Override
    @Transactional
    public List<Request> findByApprover(Integer approver) {
        List<Request> requests = requestRepository.findByApprover(approver);
        List<Request> pendings = requests.stream().filter(x -> x.getStatus().equals("PENDING")).toList();
        return pendings;
    }
}

