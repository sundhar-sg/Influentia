package com.cognizant.influentia.accountms.service;

import java.util.List;
import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cognizant.influentia.accountms.dto.*;
import com.cognizant.influentia.accountms.entity.*;
import com.cognizant.influentia.accountms.repository.*;
import com.cognizant.influentia.exception.ResourceQuotaExceededException;
import com.cognizant.influentia.subscriptionms.entity.SubscriptionPlans;
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
			accountType.setListOfAccounts(this.userSocialAccountsRepo.findListOfAccountsBasedOnAccountTypes(accountType.getId()));
			accountTypes.add(this.modelMapper.map(accountType, AccountTypeDTO.class));
		}
		return accountTypes;
	}

	@Override
	public UserSocialAccountsDTO addNewSocialAccount(UserSocialAccountsDTO userSocialAccountsDTO) throws ResourceQuotaExceededException {
		String username = userSocialAccountsDTO.getUsername();
	    UserSubscriptions subscriptionPlan = userSubscriptionsRepo.findSubscriptionByUsername(username);
	    if (subscriptionPlan == null) {
	        log.error("UserSubscriptions not found for username: {}", username);
	        throw new IllegalArgumentException("Invalid username or subscription plan not found.");
	    }
	    List<UserSocialAccounts> registeredAccounts = userSocialAccountsRepo.findListOfAccountsForUser(username);
	    if (subscriptionPlansRepo.findById(subscriptionPlan.getPlanID().getPlanID()).isPresent()) {
	        SubscriptionPlans retrievedPlan = subscriptionPlansRepo.findById(subscriptionPlan.getPlanID().getPlanID()).get();
	        String planName = retrievedPlan.getPlanName();
	        if (planName != null && planName.equalsIgnoreCase("Basic") && registeredAccounts.size() > 3) {
	            log.error("You are not allowed to add more social accounts as you have already reached the current subscription plan limit");
	            throw new ResourceQuotaExceededException("You are not allowed to add more social accounts as you have already reached the current subscription plan limit. Upgrade your plan to get unlimited social accounts registration :)");
	        }
	    } 
	    UserSocialAccounts newAccount = new UserSocialAccounts();
	    SocialAccountTypes accountType = socialAccountTypesRepo.findById(userSocialAccountsDTO.getAccountTypeID())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid Account ID"));
	    newAccount.setSocialAccountTypeId(accountType);
	    newAccount.setLoginID(userSocialAccountsDTO.getLoginID());
	    newAccount.setEncryptedPassword(passwordEncoder.encode(userSocialAccountsDTO.getPassword()));
	    newAccount.setSubscriptionName(subscriptionPlan.getPlanID().getPlanName());
	    newAccount.setAccountActivities(null);
	    newAccount.setUserName(username);
	    socialAccountTypesRepo.save(accountType);
	    this.userSocialAccountsRepo.save(newAccount);
	    SocialAccountTracker accountTrack = new SocialAccountTracker();
	    accountTrack.setAccountID(newAccount);
	    accountTrack.setAction("AccountAdded");
	    accountTrack.setDate(Date.valueOf(LocalDate.now()));
	    this.socialAccountTrackerRepo.save(accountTrack);
	    return mapToDTO(newAccount);
	}

	private UserSocialAccountsDTO mapToDTO(UserSocialAccounts userSocialAccounts) {
		if (userSocialAccounts == null) 
	        return null;		
		UserSocialAccountsDTO dto = new UserSocialAccountsDTO();
		SocialAccountTypes socialAccountType = userSocialAccounts.getSocialAccountTypeId();
	    if (socialAccountType != null) {
	        Integer accountTypeId = socialAccountType.getId();
	        dto.setAccountTypeID(accountTypeId != null ? accountTypeId.intValue() : null);
	    }
	    dto.setLoginID(userSocialAccounts.getLoginID());
	    dto.setPassword(userSocialAccounts.getEncryptedPassword());
	    dto.setUsername(userSocialAccounts.getUserName());
	    return dto;
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
		SocialAccountTracker accountStatus = new SocialAccountTracker();
		accountStatus.setAccountID(null);
		accountStatus.setAction("AccountRemoved");
		accountStatus.setDate(Date.valueOf(LocalDate.now()));
		if(isDeleted >= 1) 
			return true;
		return false;
	}

}
