package com.banking.request_service.service;

import java.util.List;
import java.util.Optional;

import com.banking.request_service.entity.Request;

public interface RequestService {

	String createRequest(Request request);

    Optional<Request> getRequestById(Integer id);

    List<Request> getAllRequests();

    Request updateRequest(Integer id, Request requestDetails);

    List<Request> findByCif();
    List<Request> findByAccountNo(String accountNo);
    List<Request> findByApprover(Integer approver);
}
