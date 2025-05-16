package com.banking.user_service.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.banking.user_service.dto.LoginRequest;
import com.banking.user_service.dto.ManagerRequest;
import com.banking.user_service.dto.UserRequest;
import com.banking.user_service.dto.UserResponse;
import com.banking.user_service.entity.Role;
import com.banking.user_service.entity.Token;
import com.banking.user_service.entity.User;
import com.banking.user_service.exception.InvalidLoginException;
import com.banking.user_service.exception.ManagerNotFoundException;
import com.banking.user_service.exception.UserNotFoundException;
import com.banking.user_service.repository.UserRepository;
import com.banking.user_service.security.JwtService;

@Service
public class UserServiceImpl implements UserService{

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepo,PasswordEncoder passwordEncoder,JwtService jwtService) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	private static boolean isNullOrEmpty(String value) {
	    return value == null || value.trim().isEmpty();
	}
	
	@Override
	public List<UserResponse> getAllUsers() {
		List<User> users = userRepo.findAll();
		List<UserResponse> response = new ArrayList<>();
		for(User user : users) {
			UserResponse res = entityToResponse(user);
			String managerName = null;
			if(user.getManagerId() == null) {
				managerName = "NO MANAGER ASSIGNED";
			}
			else {
				Optional<User> manager = userRepo.findById(user.getManagerId());
				if(!manager.isEmpty()) {
					managerName = manager.get().getFirstName()+" "+manager.get().getLastName();
				}
			}
			res.setManager(managerName);
			response.add(res);
		}
		return response;
	}

	@Override
	public UserResponse getUser(int id) {
		User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("No User found with id : "+id));
		UserResponse response = entityToResponse(user);
		
		String managerName = null;
		if(user.getManagerId() == null) {
			managerName = "NO MANAGER ASSIGNED";
		}
		else {
			Optional<User> manager = userRepo.findById(user.getManagerId());
			if(!manager.isEmpty()) {
				managerName = manager.get().getFirstName()+" "+manager.get().getLastName();
			}
		}
		
		response.setManager(managerName);
		return response;
	}

	@Override
	public String deleteUser(int id) {
		User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("No User found with id : "+id));
        
        // If the user being deleted is a manager
        if (user.getRole().equals(Role.MANAGER)) {
            // Find all users assigned to this manager
            List<User> assignedUsers = userRepo.findAll().stream()
                .filter(u -> u.getManagerId() != null && u.getManagerId() == id)
                .collect(Collectors.toList());
            
            // Set managerId to null for all assigned users
            assignedUsers.forEach(u -> u.setManagerId(null));
            userRepo.saveAll(assignedUsers);
        }
        
        userRepo.delete(user);
        return "User removed successfully";
	}

	@Override
	public UserResponse updateUser(UserRequest request, int id) {
		User savedUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("No User found with id : "+id));
		
		if(!isNullOrEmpty(request.getEmail())) savedUser.setEmail(request.getEmail());
		if(!isNullOrEmpty(request.getFirstName())) savedUser.setFirstName(request.getFirstName());
		if(!isNullOrEmpty(request.getLastName())) savedUser.setLastName(request.getLastName());
		if(!isNullOrEmpty(request.getPhnNo())) savedUser.setPhone(request.getPhnNo());
		if(!isNullOrEmpty(request.getUserName())) savedUser.setUserName(request.getUserName());
		if(!isNullOrEmpty(request.getPassword())) {
			savedUser.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		
		userRepo.save(savedUser);
		return entityToResponse(savedUser);
	}
	
	@Override
	public UserResponse assignManager(ManagerRequest request, int id) {
		User savedUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("No User found with id : "+id));
		
		User manager = userRepo.findById(request.getId()).orElseThrow(() -> new UserNotFoundException("No User found with id : "+id));
		
		if(manager.getFirstName().equals(request.getFirstName()) && manager.getLastName().equals(request.getLastName())) {
			savedUser.setManagerId(request.getId());
		}
		else
		{
			throw new ManagerNotFoundException("Manager request not valid!");
		}
		
		userRepo.save(savedUser);
		UserResponse response = entityToResponse(savedUser);
		response.setManager(manager.getFirstName()+" "+manager.getLastName());
		return response;
	}

	@Override
	public String saveUser(UserRequest userRequest) {
		User user = requestToEntity(userRequest);
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setCif(generateCif(11));
		userRepo.save(user);
		return "User registered successfully";
	}

	@Override
	public Token loginUser(LoginRequest loginRequest) {
		User user = userRepo.findByUserName(loginRequest.getUsername())
                .orElseThrow(() -> new InvalidLoginException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidLoginException("Invalid username or password");
        }

        String token = jwtService.generateToken(user);
        return new Token(token);
	}

	@Override
	public List<UserResponse> getAllManagers() {
		List<User> managers = userRepo.findAll().stream()
				.filter(user -> user.getRole().equals(Role.MANAGER))
				.collect(Collectors.toList());
		return managers.stream()
				.map(UserServiceImpl::entityToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public List<UserResponse> getUsersByManager(int managerId) {
		List<User> users = userRepo.findAll().stream()
				.filter(user -> user.getManagerId() != null && user.getManagerId() == managerId)
				.collect(Collectors.toList());
		User manager = userRepo.findById(managerId).orElseThrow(() -> new UserNotFoundException("Manager is not there with id : "+managerId));
		
		return users.stream()
				.map(UserServiceImpl::entityToResponse)
				.map(u -> { u.setManager(manager.getFirstName()+" "+manager.getLastName());
					return u;})
				.collect(Collectors.toList());
	}

	public static UserResponse entityToResponse(User user) {
	    if (user == null) {
	        return null;
	    }

	    return UserResponse.builder()
	            .cif(user.getCif())
	            .email(user.getEmail())
	            .firstName(user.getFirstName())
	            .lastName(user.getLastName())
	            .phnNo(user.getPhone())
	            .role(user.getRole())
	            .userName(user.getUserName())
	            .build();
	}
	
	public static User requestToEntity(UserRequest request) {
	    if (request == null) {
	        throw new IllegalArgumentException("UserRequest cannot be null");
	    }

	    System.out.println(request.getRole());
	    
	    return User.builder()
	            .firstName(request.getFirstName())
	            .lastName(request.getLastName())
	            .userName(request.getUserName())
	            .phone(request.getPhnNo())
	            .password(request.getPassword())
	            .email(request.getEmail())
	            .role(request.getRole())
	            .build();
	}
	
	private static String generateCif(int length) {
	    SecureRandom random = new SecureRandom();
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < length; i++) {
	        sb.append(random.nextInt(10)); // appends a digit between 0-9
	    }
	    return sb.toString();
	}
}
