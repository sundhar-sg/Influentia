package com.cognizant.influentia.security.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.influentia.security.dto.RoleDTO;
import com.cognizant.influentia.security.dto.SignupDTO;
import com.cognizant.influentia.security.dto.UserDTO;
import com.cognizant.influentia.security.entity.User;
import com.cognizant.influentia.security.jwtconfig.JwtResponse;
import com.cognizant.influentia.security.jwtconfig.JwtUtils;
import com.cognizant.influentia.security.service.UserService;

import io.jsonwebtoken.security.InvalidKeyException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Validated
@Slf4j
public class SecurityController {
	
	@Autowired
	private DaoAuthenticationProvider authManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDTO) throws InvalidKeyException, NoSuchAlgorithmException {
		try {
			authManager.authenticate(
				new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
			);
			UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getUsername());
			User currentUser = this.userService.findByUsername(userDTO.getUsername());
			String token = jwtUtils.generateToken(userDetails.getUsername(), (currentUser.getFirstName()+ " " +currentUser.getLastName()), currentUser.getRoles());
			return ResponseEntity.ok(new JwtResponse(token));
		} catch (AuthenticationException ex) {
			log.error("Invalid Username or Password entered");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
		}
	}
	
	@PostMapping("/logout")
	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();
	}
	
	@PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> signUp(@RequestBody @Valid SignupDTO signupDTO) {
		RoleDTO roleDTO = new RoleDTO("User");
		UserDTO signupState = this.userService.addNewUser(signupDTO, List.of(roleDTO));
		if(signupState != null) {
			log.info("Signing up of new user is successful");
			return new ResponseEntity<String>("Signup Success", HttpStatus.OK);
		}
		log.error("Signing of new user is not successfully done");
		return null;
	}
}