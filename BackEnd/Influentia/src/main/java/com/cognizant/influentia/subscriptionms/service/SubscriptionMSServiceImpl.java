package com.cognizant.influentia.subscriptionms.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.influentia.subscriptionms.dto.CancelUserSubscriptionDTO;
import com.cognizant.influentia.subscriptionms.dto.NewUserSubscriptionDTO;
import com.cognizant.influentia.subscriptionms.dto.RenewUserSubscriptionDTO;
import com.cognizant.influentia.subscriptionms.dto.SubscriptionPlansDTO;
import com.cognizant.influentia.subscriptionms.dto.UserSubscriptionsDTO;
import com.cognizant.influentia.subscriptionms.entity.SubscriptionCancellations;
import com.cognizant.influentia.subscriptionms.entity.SubscriptionPlans;
import com.cognizant.influentia.subscriptionms.entity.UserSubscriptions;
import com.cognizant.influentia.subscriptionms.repository.SubscriptionCancellationsRepo;
import com.cognizant.influentia.subscriptionms.repository.SubscriptionPlansRepo;
import com.cognizant.influentia.subscriptionms.repository.UserSubscriptionsRepo;

@Service
public class SubscriptionMSServiceImpl implements SubscriptionMSService {
	
	@Autowired
	private SubscriptionPlansRepo subscriptionPlansRepo;
	
	@Autowired
	private UserSubscriptionsRepo userSubscriptionsRepo;
	
	@Autowired
	private SubscriptionCancellationsRepo subscriptionCancellationsRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<SubscriptionPlansDTO> getSubscriptionPlans() {
		// TODO Auto-generated method stub
		List<SubscriptionPlansDTO> subscriptionPlansList = new ArrayList<>();
//		TypeMap<SubscriptionPlans, SubscriptionPlansDTO> subscriptionPlanMap = this.modelMapper.createTypeMap(SubscriptionPlans.class, SubscriptionPlansDTO.class);
//		TypeMap<UserSubscriptions, UserSubscriptionsDTO> userSubscriptionsMap = this.modelMapper.createTypeMap(UserSubscriptions.class, UserSubscriptionsDTO.class);
//		subscriptionPlanMap.addMapping(SubscriptionPlans::getListOfUsers, SubscriptionPlansDTO::setListOfUsers);
		for(SubscriptionPlans subsPlan : this.subscriptionPlansRepo.findAll()) {
			subsPlan.setListOfUsers(this.userSubscriptionsRepo.findUserSubscriptionsByPlanID(subsPlan.getPlanID()));
			subscriptionPlansList.add(this.modelMapper.map(subsPlan, SubscriptionPlansDTO.class));
		}
		return subscriptionPlansList;
	}

	@Override
	public UserSubscriptions purchaseSubscription(NewUserSubscriptionDTO userSubscription) {
		// TODO Auto-generated method stub
		int validityDuration = userSubscription.getValidityDuration();
		double finalAmount = 0;
		int pricePerMonth = subscriptionPlansRepo.findById(userSubscription.getPlanID()).get().getPricePerMonth();
		if(validityDuration == 3) {
			if(subscriptionPlansRepo.findById(userSubscription.getPlanID()).get().getPlanName().equalsIgnoreCase("Pro"))
				finalAmount = (pricePerMonth * validityDuration) - (pricePerMonth * validityDuration * 0.05);
			else
				finalAmount = (pricePerMonth * validityDuration) - (pricePerMonth * validityDuration * 0.03);
		} else if(validityDuration == 6) {
			if(subscriptionPlansRepo.findById(userSubscription.getPlanID()).get().getPlanName().equalsIgnoreCase("Pro"))
				finalAmount = (pricePerMonth * validityDuration) - (pricePerMonth * validityDuration * 0.1);
			else
				finalAmount = (pricePerMonth * validityDuration) - (pricePerMonth * validityDuration * 0.05);
		} else if(validityDuration == 12) {
			if(subscriptionPlansRepo.findById(userSubscription.getPlanID()).get().getPlanName().equalsIgnoreCase("Pro"))
				finalAmount = (pricePerMonth * validityDuration) - (pricePerMonth * validityDuration * 0.15);
			else
				finalAmount = (pricePerMonth * validityDuration) - (pricePerMonth * validityDuration * 0.08);
		}
		UserSubscriptions newSubscription = this.modelMapper.map(userSubscription, UserSubscriptions.class);
		newSubscription.setAmountPaid(finalAmount);
		newSubscription.setSubscriptionStartDate(Date.valueOf(LocalDate.now()));
		newSubscription.setSubscriptionEndDate(Date.valueOf(LocalDate.now().plusMonths(validityDuration)));
		newSubscription.setSubscriptionStatus("New");
		newSubscription.setSubscriptionCancel(null);
		return this.userSubscriptionsRepo.save(newSubscription);
	}

	@Override
	public UserSubscriptions renewSubscription(int subscriptionID, RenewUserSubscriptionDTO userSubscription) {
		// TODO Auto-generated method stub
		UserSubscriptions fetchedResult = this.userSubscriptionsRepo.findById(subscriptionID).get();
		fetchedResult = this.modelMapper.map(userSubscription, UserSubscriptions.class);
		int validityDuration = userSubscription.getValidityDuration();
		fetchedResult.setSubscriptionStartDate(Date.valueOf(LocalDate.now()));
		fetchedResult.setSubscriptionEndDate(Date.valueOf(LocalDate.now().plusMonths(validityDuration)));
		fetchedResult.setSubscriptionCancel(null);
		return this.userSubscriptionsRepo.save(fetchedResult);
	}

	@Override
	public SubscriptionCancellations cancelSubcription(int subscriptionID, CancelUserSubscriptionDTO userSubscription) {
		// TODO Auto-generated method stub
		SubscriptionCancellations cancelSubs = this.subscriptionCancellationsRepo.save(this.modelMapper.map(userSubscription, SubscriptionCancellations.class));
		UserSubscriptions fetchedResult = this.userSubscriptionsRepo.findById(subscriptionID).get();
		fetchedResult.setSubscriptionCancel(cancelSubs);
		this.userSubscriptionsRepo.save(fetchedResult);
		return cancelSubs;
	}

	@Override
	public UserSubscriptionsDTO getSubscriptionPlanByUsername(String username) {
		// TODO Auto-generated method stub
		return this.modelMapper.map(this.userSubscriptionsRepo.findSubscriptionByUsername(username), UserSubscriptionsDTO.class);
	}
}