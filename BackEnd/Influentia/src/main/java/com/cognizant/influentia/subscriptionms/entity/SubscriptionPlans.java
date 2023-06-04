package com.cognizant.influentia.subscriptionms.entity;

import java.util.List;

import jakarta.persistence.*;
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
	private String planName;
	
	@Column(name = "pricepermonth", nullable = false, updatable = true)
	private int pricePerMonth;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = false, mappedBy = "planID")
	private List<UserSubscriptions> listOfUsers;

	public SubscriptionPlans(int planID, String planName, int pricePerMonth) {
		super();
		this.planID = planID;
		this.planName = planName;
		this.pricePerMonth = pricePerMonth;
	}
}