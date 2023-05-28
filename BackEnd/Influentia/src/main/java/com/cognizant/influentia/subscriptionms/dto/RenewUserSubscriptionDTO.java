package com.cognizant.influentia.subscriptionms.dto;

import java.util.*;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
public class RenewUserSubscriptionDTO {

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
	@Pattern(regexp = "(?i)(?:renewed)", message = "The Subscription status can be only Renewed for existing subscribers if you wish to proceed with exiting plan")
	private String subscriptionStatus;
}