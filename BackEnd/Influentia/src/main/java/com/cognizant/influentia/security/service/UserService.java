package com.cognizant.influentia.security.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.cognizant.influentia.security.dto.RoleDTO;
import com.cognizant.influentia.security.dto.SignupDTO;
import com.cognizant.influentia.security.dto.UserDTO;
import com.cognizant.influentia.security.entity.User;

public interface UserService extends UserDetailsService {

	public User findByUsername(String username);
	
	public UserDTO addNewUser(SignupDTO signupDTO, List<RoleDTO> roleDTO);
}