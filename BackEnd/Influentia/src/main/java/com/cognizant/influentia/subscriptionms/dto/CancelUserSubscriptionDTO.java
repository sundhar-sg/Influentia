package com.cognizant.influentia.subscriptionms.dto;

import java.time.LocalDate;

import com.cognizant.influentia.subscriptionms.entity.UserSubscriptions;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancelUserSubscriptionDTO {
	
	@NotNull
	@NotEmpty
	private UserSubscriptions subscriptionID;
	
	@NotNull
	@NotEmpty
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private LocalDate cancellationDate;
	
	@NotNull
	@NotEmpty
	private String cancellationReason;
}