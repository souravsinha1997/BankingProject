package com.banking.user_service.dto;

import com.banking.user_service.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserRequest {

	@NotBlank(message = "First name is required")
    @Size(max = 100)
	private String firstName;
	
	@NotBlank(message = "Last name is required")
    @Size(max = 100)
	private String lastName;
	
	@NotBlank(message = "Username is required")
    @Size(max = 20)
	private String userName;
	
	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
	private String phnNo;
	
	@NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
	private String password;
	
	@NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
	private String email;
	
	@NotNull(message = "Role is required")
	private Role role;
}
