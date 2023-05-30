package com.cognizant.influentia.subscriptionms.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class NewUserSubscriptionDTO {
	
	@NotNull
	private int planID;

	@NotEmpty
	@NotNull
	private String userName;
	
	@NotNull
	private int validityDuration;
	
	@NotEmpty
	@NotNull
	@Pattern(regexp = "^(?i)(Card|NetBanking)$", message = "The Payment mode can be either Card or NetBanking")
	private String paymentMode;
	
	public NewUserSubscriptionDTO(int planID, String userName, int validityDuration, String paymentMode) {
		super();
		this.planID = planID;
		this.userName = userName;
		this.validityDuration = validityDuration;
		this.paymentMode = paymentMode;
	}
}