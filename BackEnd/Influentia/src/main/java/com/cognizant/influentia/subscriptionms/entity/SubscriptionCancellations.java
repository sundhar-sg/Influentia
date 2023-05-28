package com.cognizant.influentia.subscriptionms.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscription_cancellations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionCancellations {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne(mappedBy = "subscriptionCancel")
	private UserSubscriptions subscriptionID;
	
	@Column(name = "cancellation_date", nullable = false, updatable = true)
	@Temporal(TemporalType.DATE)
	private Date cancellationDate;
	
	@Column(name = "cancellation_reason", nullable = false, updatable = true)
	private String cancellationReason;
}