package com.cognizant.influentia.subscriptionms.service;

import java.util.List;

import com.cognizant.influentia.subscriptionms.dto.CancelUserSubscriptionDTO;
import com.cognizant.influentia.subscriptionms.dto.NewUserSubscriptionDTO;
import com.cognizant.influentia.subscriptionms.dto.RenewUserSubscriptionDTO;
import com.cognizant.influentia.subscriptionms.dto.SubscriptionPlansDTO;
import com.cognizant.influentia.subscriptionms.dto.UserSubscriptionsDTO;
import com.cognizant.influentia.subscriptionms.entity.SubscriptionCancellations;
import com.cognizant.influentia.subscriptionms.entity.UserSubscriptions;

public interface SubscriptionMSService {

	public List<SubscriptionPlansDTO> getSubscriptionPlans();
	
	public UserSubscriptions purchaseSubscription(NewUserSubscriptionDTO userSubscription);
	
	public UserSubscriptions renewSubscription(int subscriptionID, RenewUserSubscriptionDTO userSubscription);
	
	public SubscriptionCancellations cancelSubcription(int subscriptionID, CancelUserSubscriptionDTO userSubscription);
	
	public UserSubscriptionsDTO getSubscriptionPlanByUsername(String username);
}