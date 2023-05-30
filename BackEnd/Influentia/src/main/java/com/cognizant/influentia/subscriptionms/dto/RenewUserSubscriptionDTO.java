package com.cognizant.influentia.subscriptionms.dto;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class RenewUserSubscriptionDTO {

	@Id
	private int planID;
	
	@NotEmpty
	@NotNull
	private String userName;
	
	@NotEmpty
	@NotNull
	private int validityDuration;
	
	@NotEmpty
	@NotNull
	private double amountPaid;
	
	@NotEmpty
	@NotNull
	@Pattern(regexp = "^(?i)(Card|NetBanking)$", message = "The Payment mode can be either Card or NetBanking")
	private String paymentMode;
	
	@NotEmpty
	@NotNull
	@Pattern(regexp = "^(?i)(Renewed)$", message = "The Subscription status can be only Renewed for existing subscribers if you wish to proceed with extending the existing plan")
	private String subscriptionStatus;

	public RenewUserSubscriptionDTO(int planID, String userName, int validityDuration, String paymentMode, String subscriptionStatus) {
		super();
		this.planID = planID;
		this.userName = userName;
		this.validityDuration = validityDuration;
		this.paymentMode = paymentMode;
		this.subscriptionStatus = subscriptionStatus;
	}
}