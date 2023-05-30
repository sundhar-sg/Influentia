package com.cognizant.influentia.accountms.service;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognizant.influentia.accountms.dto.*;
import com.cognizant.influentia.accountms.entity.*;
import com.cognizant.influentia.accountms.repository.*;
import com.cognizant.influentia.exception.ResourceQuotaExceededException;
import com.cognizant.influentia.subscriptionms.entity.UserSubscriptions;
import com.cognizant.influentia.subscriptionms.repository.*;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountMSServiceImpl implements AccountMSService {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	SocialAccountTrackerRepo socialAccountTrackerRepo;
	
	@Autowired
	SocialAccountTypesRepo socialAccountTypesRepo;
	
	@Autowired
	UserSocialAccountsRepo userSocialAccountsRepo;
	
	@Autowired
	UserSubscriptionsRepo userSubscriptionsRepo;
	
	@Autowired
	SubscriptionPlansRepo subscriptionPlansRepo;
	
	@Override
	public List<AccountTypeDTO> getAllAccountTypes() {
		// TODO Auto-generated method stub
		List<AccountTypeDTO> accountTypes = new ArrayList<>();
		for(SocialAccountTypes accountType : socialAccountTypesRepo.findAll()) {
			accountTypes.add(this.modelMapper.map(accountType, AccountTypeDTO.class));
		}
		return accountTypes;
	}

	@Override
	public UserSocialAccounts addNewSocialAccount(UserSocialAccountsDTO userSocialAccountsDTO) throws ResourceQuotaExceededException {
		
		// TODO Auto-generated method stub
		String username = userSocialAccountsDTO.getUsername();
		UserSubscriptions subscriptionPlan = userSubscriptionsRepo.findSubscriptionByUsername(username);
		List<UserSocialAccounts> registeredAccounts = userSocialAccountsRepo.findListOfAccountsForUser(username);
		if(subscriptionPlansRepo.findById(subscriptionPlan.getSubscriptionID()).get().getPlanName().equalsIgnoreCase("Basic") && registeredAccounts.size() > 3) {
			log.error("Not Allowed for adding new social account as the limit has been already reached. Need to add more social accounts try to upgrade your PLAN!!!");
			throw new ResourceQuotaExceededException("With your current subscription plan, you can't add any new social account to your subscription. Try upgrading your plan for unlimited social accout add benefit :)");
		}
		userSocialAccountsDTO.setEncryptedPassword(passwordEncoder.encode(userSocialAccountsDTO.getEncryptedPassword()));
		return userSocialAccountsRepo.save(this.modelMapper.map(userSocialAccountsDTO, UserSocialAccounts.class));
	}

	@Override
	public List<UserSocialAccountsDTO> socialAccountsRegisteredForUser(String username) {
		// TODO Auto-generated method stub
		List<UserSocialAccountsDTO> registeredAccounts = new ArrayList<>();
		for(UserSocialAccounts regAccount : userSocialAccountsRepo.findListOfAccountsForUser(username)) {
			registeredAccounts.add(this.modelMapper.map(regAccount, UserSocialAccountsDTO.class));
		}
		return registeredAccounts;
	}

	@Override
	public boolean removeSocialAccountByUser(int accountTypeId, String username) {
		// TODO Auto-generated method stub
		int isDeleted = userSocialAccountsRepo.deleteAccount(accountTypeId, username);
		if(isDeleted >= 1) 
			return true;
		return false;
	}

}
