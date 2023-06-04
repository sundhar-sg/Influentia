package com.cognizant.influentia.subscriptionms.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancelUserSubscriptionDTO {
	
	@NotNull
	private int subscriptionID;
	
	@NotNull
	@NotEmpty
	private String cancellationReason;
}