package com.banking.transaction_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.banking.transaction_service.client.dto.UserResponse;



@FeignClient(name = "USER-SERVICE")
public interface UserClient {

	@GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable int id);
}
