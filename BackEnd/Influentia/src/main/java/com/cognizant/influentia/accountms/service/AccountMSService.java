package com.cognizant.influentia.accountms.service;

import java.util.List;

import com.cognizant.influentia.accountms.dto.AccountTypeDTO;
import com.cognizant.influentia.accountms.dto.UserSocialAccountsDTO;
import com.cognizant.influentia.accountms.entity.*;
import com.cognizant.influentia.exception.ResourceQuotaExceededException;

public interface AccountMSService {

	public List<AccountTypeDTO> getAllAccountTypes();
	
	public UserSocialAccounts addNewSocialAccount(UserSocialAccountsDTO userSocialAccountsDTO) throws ResourceQuotaExceededException;
	
	public List<UserSocialAccountsDTO> socialAccountsRegisteredForUser(String username);
	
	public boolean removeSocialAccountByUser(int accountTypeId, String username);
}