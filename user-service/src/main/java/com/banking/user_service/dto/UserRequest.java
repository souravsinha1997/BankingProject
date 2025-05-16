package com.banking.user_service.dto;

import com.banking.user_service.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserRequest {

	private String firstName;
	private String lastName;
	private String userName;
	private String phnNo;
	private String password;
	private String email;
	private Role role;
}
