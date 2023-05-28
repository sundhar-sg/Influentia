package com.cognizant.influentia.subscriptionms.entity;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "usersubscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptions {

	@Id
	@Column(name = "subscription_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int subscriptionID;
	
	@Column(name = "username", nullable = false, updatable = true)
	private String userName;
	
	@ManyToOne
	@JoinColumn(name = "planid")
	private SubscriptionPlans planID;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "subscriptionstartdate", nullable = false, updatable = true)
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	@Future(message = "The Subscription Start Date must be a future date")
	private Date subscriptionStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "subscriptionenddate", nullable = false, updatable = true)
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	@Future(message = "The Subscription End Date must be a future date")
	private Date subscriptionEndDate;
	
	@Column(name = "amountpaid", nullable = false, updatable = true)
	private int amountPaid;
	
	@Column(name = "payment_mode", nullable = false, updatable = true)
	@Pattern(regexp = "^(?i)(NetBanking|Card)$", message = "The Payment Mode allowed for purchasing the Subscription Plan should be of either Net Banking (or) Card")
	private String paymentMode;
	
	@Column(name = "subscription_status", nullable = false, updatable = true)
	@Pattern(regexp = "^(?i)(New|Renewed|Cancelled)$", message = "The Subscription Status should include values of either New (or) Renewed (or) Cancelled")
	private String subscriptionStatus;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "subscription_cancellations_id")
	private SubscriptionCancellations subscriptionCancel;
}