package com.cognizant.influentia.security.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDTO {

	@NotEmpty
	@NotNull
	private String username;
	
	@NotEmpty
	@NotNull
	private String fullName;
	
	@NotNull
	private Collection<? extends GrantedAuthority> specifiedRoles;
}