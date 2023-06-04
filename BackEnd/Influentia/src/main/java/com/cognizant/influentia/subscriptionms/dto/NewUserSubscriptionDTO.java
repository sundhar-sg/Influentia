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
	@Pattern(regexp = "^(1|3|6|12)$", message = "The Validity duration can be either 1, 3, 6 (or) 12 months / 1 year")
	private String validityDuration;
	
	@NotEmpty
	@NotNull
	@Pattern(regexp = "^(?i)(Card|NetBanking)$", message = "The Payment mode can be either Card or NetBanking")
	private String paymentMode;
	
	public NewUserSubscriptionDTO(int planID, String userName, String validityDuration, String paymentMode) {
		super();
		this.planID = planID;
		this.userName = userName;
		this.validityDuration = validityDuration;
		this.paymentMode = paymentMode;
	}
}