package com.banking.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.user_service.dto.ManagerRequest;
import com.banking.user_service.dto.UserResponse;
import com.banking.user_service.exception.UserNotFoundException;
import com.banking.user_service.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@Validated
public class ManagerController {
    
    private final UserService userService;
    
    public ManagerController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> assignManager(@RequestBody @Validated ManagerRequest request, @PathVariable int id) {
        try {
            UserResponse response = userService.assignManager(request, id);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllManagers() {
        try {
            List<UserResponse> managers = userService.getAllManagers();
            return ResponseEntity.ok(managers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserResponse>> getUsersByManager(@PathVariable int id) {
        try {
            List<UserResponse> users = userService.getUsersByManager(id);
            return ResponseEntity.ok(users);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
