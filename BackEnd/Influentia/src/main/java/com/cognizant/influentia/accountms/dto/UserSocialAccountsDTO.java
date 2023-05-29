package com.cognizant.influentia.accountms.dto;

import java.util.List;

import com.cognizant.influentia.accountms.entity.SocialAccountTracker;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSocialAccountsDTO {
	
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
	
	private List<SocialAccountTracker> accountActivities;
}