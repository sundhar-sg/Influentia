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
	
	@NotEmpty
	@Pattern(regexp = "^(?i)(Pro|Basic)$", message = "The available plans for availing subscription is either Pro (or) Basic")
	private String planName;
	
	@NotEmpty
	@Pattern(regexp = "^(25|10)$", message = "The Price per month of Subscription plan should be of either $25 (or) $10")
	private int pricePerMonth;
	
	@NotEmpty
	@NotNull
	private List<UserSubscriptionsDTO> listOfUsers;
}