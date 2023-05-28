package com.cognizant.influentia.subscriptionms.dto;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlansDTO {

	@Id
	@NotEmpty
	private int planID;
	
	@Pattern(regexp = "(?i)(?:pro|basic)")
	@NotEmpty
	private String planName;
	
	@Pattern(regexp = "^(25|10)$")
	@NotEmpty
	private int pricePerMonth;
	
	@NotEmpty
	@NotNull
	private List<UserSubscriptionsDTO> listOfUsers;
}