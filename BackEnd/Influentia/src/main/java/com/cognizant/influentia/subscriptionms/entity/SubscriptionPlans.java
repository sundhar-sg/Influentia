package com.cognizant.influentia.subscriptionms.entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "subscriptionplans")
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionPlans {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "planid")
	private int planID;
	
	@Column(name = "planname", nullable = false, updatable = true)
	@Pattern(regexp = "^(?i)(Pro|Basic)$", message = "The Plan Name should be of either Pro (or) Basic")
	private String planName;
	
	@Column(name = "pricepermonth", nullable = false, updatable = true)
	@Pattern(regexp = "^(25|10)$", message = "The Price per month of Subscription plan should be of either $25 (or) $10")
	private int pricePerMonth;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
	@JoinColumn(name = "planid")
	private List<UserSubscriptions> listOfUsers;

	public SubscriptionPlans(int planID,
			@Pattern(regexp = "^(?i)(Pro|Basic)$", message = "The Plan Name should be of either Pro (or) Basic") String planName,
			@Pattern(regexp = "^(25|10)$", message = "The Price per month of Subscription plan should be of either $25 (or) $10") int pricePerMonth) {
		super();
		this.planID = planID;
		this.planName = planName;
		this.pricePerMonth = pricePerMonth;
	}
}