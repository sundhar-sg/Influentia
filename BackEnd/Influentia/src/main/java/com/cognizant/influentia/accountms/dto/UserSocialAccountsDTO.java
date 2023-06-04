package com.cognizant.influentia.accountms.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSocialAccountsDTO {
	
	@NotNull
	private int accountTypeID;
	
	@NotEmpty
	private String loginID;
	
	@NotEmpty
	private String password;
	
	@NotEmpty
	private String username;
}