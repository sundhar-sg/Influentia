package com.cognizant.influentia.security.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDTO {
	
	@NotEmpty
	@NotNull
	@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name should consist of only text")
	private String firstname;
	
	@NotEmpty
	@NotNull
	@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name should consist of only text")
	private String lastname;

	@NotEmpty
	@NotNull
	@Pattern(regexp = "^[a-zA-Z0-9\\s@_$-]+$", message = "Username should consist of only text and numbers and allowed special characters are @, _, -, $")
	private String username;
	
	@NotEmpty
	@NotNull
	@Pattern(regexp = "^(?=.{8,}$)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*\\W).*$", message = "The Password must contain atleast one Uppercase, lowercase, number and a special character")
	private String password;
	
	@NotNull
	private int planID;
	
	@NotEmpty
	@NotNull
	private String validityDuration;
	
	@NotEmpty
	@NotNull
	private String paymentMode;
}