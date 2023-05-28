package com.cognizant.influentia.subscriptionms.dto;

import java.util.*;

import com.cognizant.influentia.subscriptionms.entity.SubscriptionCancellations;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancelUserSubscriptionDTO {

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
	@Pattern(regexp = "(?i)(?:cancel)", message = "The Subscription status can be only Cancel for subscribers if they wish to cancel their existing subscription")
	private String subscriptionStatus;
	
	@NotEmpty
	@NotNull
	private SubscriptionCancellations subsCancelID;
}