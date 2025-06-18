package com.banking.request_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.request_service.entity.Request;

public interface RequestRepository extends JpaRepository<Request,Integer>{
    List<Request> findByCif(String cif);

    List<Request> findByAccountNo(String accountNo);

    List<Request> findByApprover(Integer approver);
}
