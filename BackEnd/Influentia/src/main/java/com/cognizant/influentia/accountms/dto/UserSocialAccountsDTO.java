package com.cognizant.influentia.accountms.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSocialAccountsDTO {

	@Id
	@NotEmpty
	private int id;
	
	@NotEmpty
	private AccountTypeDTO accountTypeID;
	
	@NotEmpty
	private String loginID;
	
	@NotEmpty
	private String encryptedPassword;
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	@Pattern(regexp = "^(?i)(Pro|Basic)$", message = "The Subscription Name can only be either Pro (or) Basic")
	private String subscriptionName;
}