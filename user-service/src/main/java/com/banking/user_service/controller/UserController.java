package com.banking.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.user_service.dto.LoginRequest;
import com.banking.user_service.dto.MessageResponse;
import com.banking.user_service.dto.UserRequest;
import com.banking.user_service.dto.UserResponse;
import com.banking.user_service.entity.Token;
import com.banking.user_service.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService=userService;
	}
	
	@PostMapping("/signIn")
    public ResponseEntity<Token> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("/signUp")
    public ResponseEntity<MessageResponse> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(new MessageResponse(userService.saveUser(userRequest)));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}
