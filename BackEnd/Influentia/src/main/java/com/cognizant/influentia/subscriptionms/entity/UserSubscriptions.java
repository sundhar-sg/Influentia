package com.cognizant.influentia.subscriptionms.entity;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "usersubscriptions")
@Getter
@Setter
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
	private Date subscriptionStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "subscriptionenddate", nullable = false, updatable = true)
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	@Future(message = "The Subscription End Date must be a future date")
	private Date subscriptionEndDate;
	
	@Column(name = "amountpaid", nullable = false, updatable = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
	private Double amountPaid;
	
	@Column(name = "payment_mode", nullable = false, updatable = true)
	@Pattern(regexp = "^(?i)(NetBanking|Card)$", message = "The Payment Mode allowed for purchasing the Subscription Plan should be of either Net Banking (or) Card")
	private String paymentMode;
	
	@Column(name = "subscription_status", nullable = false, updatable = true)
	@Pattern(regexp = "^(?i)(New|Renewed|Cancelled)$", message = "The Subscription Status should include values of either New (or) Renewed (or) Cancelled")
	private String subscriptionStatus;
	
	@OneToOne
	@JoinColumn(name = "subscription_cancel_id")
	private SubscriptionCancellations subscriptionCancel;

	public UserSubscriptions(String userName, SubscriptionPlans planID, Date subscriptionStartDate, Date subscriptionEndDate, Double amountPaid, String paymentMode, String subscriptionStatus) {
		super();
		this.userName = userName;
		this.planID = planID;
		this.subscriptionStartDate = subscriptionStartDate;
		this.subscriptionEndDate = subscriptionEndDate;
		this.amountPaid = amountPaid;
		this.paymentMode = paymentMode;
		this.subscriptionStatus = subscriptionStatus;
	}
}