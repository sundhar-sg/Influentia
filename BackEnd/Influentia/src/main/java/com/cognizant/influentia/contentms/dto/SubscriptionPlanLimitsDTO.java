package com.cognizant.influentia.contentms.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanLimitsDTO {

	@NotEmpty
	@Pattern(regexp = "^(?i)(Pro|Basic)$", message = "The Subscription Plan Name should be any one of Pro and Basic subscriptions")
	private String subscriptionPlanName;
	
	@NotEmpty
	private int monthlyScheduledPostLimit;
}