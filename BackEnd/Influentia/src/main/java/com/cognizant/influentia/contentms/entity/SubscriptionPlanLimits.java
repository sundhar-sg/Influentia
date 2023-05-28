package com.cognizant.influentia.contentms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscriptionplanlimits")
@Getter
@Setter
public class SubscriptionPlanLimits {

	// Define Fields
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "planname", nullable = false, updatable = true)
	private String subscriptionPlanName;
	
	@Column(name = "monthlyscheduledpostlimit", nullable = false, updatable = true)
	private int monthlyScheduledPostLimit;
	
	// Define Constructors
	public SubscriptionPlanLimits() {
		
	}

	public SubscriptionPlanLimits(String subscriptionPlanName, int monthlyScheduledPostLimit) {
		super();
		this.subscriptionPlanName = subscriptionPlanName;
		this.monthlyScheduledPostLimit = monthlyScheduledPostLimit;
	}

	// Define toString() method
	@Override
	public String toString() {
		return "SubscriptionPlanLimits [id=" + id + ", subscriptionPlanName=" + subscriptionPlanName
				+ ", monthlyScheduledPostLimit=" + monthlyScheduledPostLimit + "]";
	}	
}