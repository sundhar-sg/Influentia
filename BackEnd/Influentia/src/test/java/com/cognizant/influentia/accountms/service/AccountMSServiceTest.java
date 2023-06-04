package com.cognizant.influentia.accountms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import com.cognizant.influentia.accountms.dto.AccountTypeDTO;
import com.cognizant.influentia.accountms.dto.UserSocialAccountsDTO;
import com.cognizant.influentia.accountms.entity.SocialAccountTypes;
import com.cognizant.influentia.accountms.entity.UserSocialAccounts;
import com.cognizant.influentia.accountms.repository.SocialAccountTrackerRepo;
import com.cognizant.influentia.accountms.repository.SocialAccountTypesRepo;
import com.cognizant.influentia.accountms.repository.UserSocialAccountsRepo;
import com.cognizant.influentia.config.AccountMSDataSourceConfiguration;
import com.cognizant.influentia.exception.ResourceQuotaExceededException;
import com.cognizant.influentia.subscriptionms.entity.SubscriptionPlans;
import com.cognizant.influentia.subscriptionms.entity.UserSubscriptions;
import com.cognizant.influentia.subscriptionms.repository.SubscriptionPlansRepo;
import com.cognizant.influentia.subscriptionms.repository.UserSubscriptionsRepo;

@DataJpaTest
@ContextConfiguration(classes = AccountMSDataSourceConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class AccountMSServiceTest {
	
	@Mock
	private UserSocialAccountsRepo userAccountsRepo;
	
	@Mock
	private SocialAccountTypesRepo socialAccTypesRepo;
	
	@Mock
	private SocialAccountTrackerRepo socialAccTracker;
	
	@Mock
	private UserSubscriptionsRepo userSubscriptionsRepo;
	
	@Mock
	private SubscriptionPlansRepo subscriptionPlansRepo;
	
	@Mock
	private SubscriptionPlans subscriptionPlans;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private AccountMSServiceImpl accountMSService;

	@Test
	void testGetAllAccountTypes() {
		List<SocialAccountTypes> listAllAccountTypes = new ArrayList<>();
		
		when(this.socialAccTypesRepo.findAll()).thenReturn(listAllAccountTypes);
		List<AccountTypeDTO> socialAccTypesByService = this.accountMSService.getAllAccountTypes();
		List<AccountTypeDTO> socialAccTypesByRepo = new ArrayList<>();
		for(SocialAccountTypes accountType : listAllAccountTypes) {
			socialAccTypesByRepo.add(this.modelMapper.map(accountType, AccountTypeDTO.class));
		}
		verify(this.socialAccTypesRepo).findAll();
		assertEquals(socialAccTypesByRepo, socialAccTypesByService);
	}

	@Test
	void testAddNewSocialAccount_ValidSubscription_BelowAccountLimit() throws ResourceQuotaExceededException {
		UserSocialAccountsDTO userSocialAccountsDTO = new UserSocialAccountsDTO();
		userSocialAccountsDTO.setUsername("testUser");
		userSocialAccountsDTO.setAccountTypeID(1);
		userSocialAccountsDTO.setLoginID("testLogin");
		userSocialAccountsDTO.setPassword("testPassword");
		
		UserSubscriptions userSubscriptionPlan = new UserSubscriptions();
		userSubscriptionPlan.setPlanID(new SubscriptionPlans(2, "Basic", 10));
		when(userSubscriptionsRepo.findSubscriptionByUsername("testUser")).thenReturn(userSubscriptionPlan);
		
		List<UserSocialAccounts> registerAccounts = new ArrayList<>();
		when(this.userAccountsRepo.findListOfAccountsForUser("testUser")).thenReturn(registerAccounts);
		
		when(this.subscriptionPlansRepo.findById(2)).thenReturn(Optional.of(new SubscriptionPlans()));
		
		SocialAccountTypes accountType = new SocialAccountTypes();
		when(this.socialAccTypesRepo.findById(1)).thenReturn(Optional.of(accountType));
		
		when(this.passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");
		
		UserSocialAccounts savedAccount = new UserSocialAccounts();
		when(this.userAccountsRepo.save(any(UserSocialAccounts.class))).thenReturn(savedAccount);
		
		UserSocialAccountsDTO result = accountMSService.addNewSocialAccount(userSocialAccountsDTO);
		assertNotNull(result);
		
		verify(userSubscriptionsRepo, times(1)).findSubscriptionByUsername("testUser");
        verify(userAccountsRepo, times(1)).findListOfAccountsForUser("testUser");
        verify(subscriptionPlansRepo, times(2)).findById(2);
        verify(socialAccTypesRepo, times(1)).findById(1);
        verify(passwordEncoder, times(1)).encode("testPassword");
        verify(userAccountsRepo, times(1)).save(any(UserSocialAccounts.class));
	}
	
	@Test
    void testAddNewSocialAccount_InvalidSubscription() {
        // Mock input data
        UserSocialAccountsDTO userSocialAccountsDTO = new UserSocialAccountsDTO();
        userSocialAccountsDTO.setUsername("testUser");
        userSocialAccountsDTO.setAccountTypeID(1);
        userSocialAccountsDTO.setLoginID("testLogin");
        userSocialAccountsDTO.setPassword("testPassword");

        // Mock repository response for invalid subscription
        when(userSubscriptionsRepo.findSubscriptionByUsername("testUser")).thenReturn(null);

        // Call the method under test and assert the exception
        assertThrows(IllegalArgumentException.class, () -> accountMSService.addNewSocialAccount(userSocialAccountsDTO));

        // Verify that the repository method was called with the correct parameter
        verify(userSubscriptionsRepo, times(1)).findSubscriptionByUsername("testUser");
    }

	@Test
	void testSocialAccountsRegisteredForUser() {
		List<UserSocialAccounts> listRegisteredAccountsForUser = new ArrayList<>();
		when(this.userAccountsRepo.findListOfAccountsForUser("sundhar_sg")).thenReturn(listRegisteredAccountsForUser);
		List<UserSocialAccountsDTO> userSocialAccountsByService = this.accountMSService.socialAccountsRegisteredForUser("sundhar_sg");
		List<UserSocialAccountsDTO> userSocialAccountsByRepo = new ArrayList<>();
		for(UserSocialAccounts userAccount : listRegisteredAccountsForUser) {
			userSocialAccountsByRepo.add(this.modelMapper.map(userAccount, UserSocialAccountsDTO.class));
		}
		verify(this.userAccountsRepo).findListOfAccountsForUser("sundhar_sg");
		assertEquals(userSocialAccountsByRepo, userSocialAccountsByService);
	}

	@Test
	void testRemoveSocialAccountByUser() {
	    int accountTypeId = 1;
	    String username = "testUser";

	    when(userAccountsRepo.deleteAccount(accountTypeId, username)).thenReturn(1);

	    boolean result = accountMSService.removeSocialAccountByUser(accountTypeId, username);

	    verify(userAccountsRepo, times(1)).deleteAccount(accountTypeId, username);

	    assertTrue(result);
	}

}