package com.banking.user_service.service;

import java.util.List;

import com.banking.user_service.dto.LoginRequest;
import com.banking.user_service.dto.ManagerRequest;
import com.banking.user_service.dto.UserRequest;
import com.banking.user_service.dto.UserResponse;
import com.banking.user_service.entity.Token;

public interface UserService {

	List<UserResponse> getAllUsers();
	UserResponse getUser(int id);
	String deleteUser(int id);
	UserResponse updateUser(UserRequest request, int id);
	String saveUser(UserRequest userRequest);
	Token loginUser(LoginRequest loginRequest);
	UserResponse assignManager(ManagerRequest request, int id);
	List<UserResponse> getUsersByManager(int managerId);
	List<UserResponse> getAllManagers();
}
