package com.cognizant.influentia.accountms.dto;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountTypeDTO {

	@Id
	@NotEmpty
	private int id;
	
	@NotEmpty
	@Pattern(regexp = "^(?i)(Facebook|Instagram|LinkedIn|Twitter|YouTube)$", message = "The account type for specifying the social account type can only be either Facebook (or) Instagram (or) LinkedIn (or) Twitter (or) YouTube")
	private String accountType;
	
	@NotEmpty
	private List<UserSocialAccountsDTO> accountTypeList;
}