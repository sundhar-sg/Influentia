package com.cognizant.influentia.subscriptionms.dto;

import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptionsDTO {

	@Id
	private int subscriptionID;
	
	@NotEmpty
	@NotNull
	private String userName;
	
	@Future
	@NotEmpty
	@NotNull
	private Date subscriptionStartDate;
	
	@Future
	@NotEmpty
	@NotNull
	private Date subscriptionEndDate;
	
	@NotEmpty
	@NotNull
	private int amountPaid;
	
	@NotEmpty
	@NotNull
	@Pattern(regexp = "(?i)(?:card|netbanking)", message = "The Payment mode can be either Card or NetBanking")
	private String paymentMode;
	
	@NotEmpty
	@NotNull
	@Pattern(regexp = "(?i)(?:new)", message = "The Subscription status can be either New, Renewed or Cancelled")
	private String subscriptionStatus;
}