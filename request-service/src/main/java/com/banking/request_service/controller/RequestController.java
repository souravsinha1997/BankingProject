package com.banking.request_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.request_service.entity.Request;
import com.banking.request_service.service.RequestService;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/user")
    public ResponseEntity<String> createRequest(@RequestBody Request request) {
        String createdRequest = requestService.createRequest(request);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable Integer id) {
        Optional<Request> request = requestService.getRequestById(id);
        return request.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/manager/{id}")
    public ResponseEntity<Request> updateRequest(@PathVariable Integer id, @RequestBody Request requestDetails) {
            Request updatedRequest = requestService.updateRequest(id, requestDetails);
            return ResponseEntity.ok(updatedRequest);
        
    }

    @GetMapping("/user")
    public ResponseEntity<List<Request>> findRequestsByCif() {
        List<Request> requests = requestService.findByCif();
        if (requests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/user/{accountNo}")
    public ResponseEntity<List<Request>> findRequestsByAccountNo(@PathVariable String accountNo) {
        List<Request> requests = requestService.findByAccountNo(accountNo);
        if (requests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/manager/{approverId}")
    public ResponseEntity<List<Request>> findRequestsByApprover(@PathVariable Integer approverId) {
        List<Request> requests = requestService.findByApprover(approverId);
        if (requests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requests);
    }
}

