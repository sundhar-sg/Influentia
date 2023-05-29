package com.cognizant.influentia.accountms.service;

import java.util.List;

import com.cognizant.influentia.accountms.dto.UserSocialAccountsDTO;
import com.cognizant.influentia.accountms.entity.*;

public interface AccountMSService {

	public List<SocialAccountTypes> getAllAccountTypes();
	
	public UserSocialAccounts addNewSocialAccount(UserSocialAccountsDTO userSocialAccountsDTO);
	
	public List<UserSocialAccounts> socialAccountsRegisteredForUser();
	
	public boolean removeSocialAccountByUser(int accountTypeId, String username);
}