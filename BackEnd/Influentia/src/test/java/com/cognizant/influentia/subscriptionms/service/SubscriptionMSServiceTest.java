package com.cognizant.influentia.subscriptionms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

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

class SubscriptionMSServiceTest {

	@Mock
	private SubscriptionPlansRepo subscriptionPlansRepo;
	
	@Mock
	private UserSubscriptionsRepo userSubscriptionsRepo;
	
	@Mock
	private SubscriptionCancellationsRepo subscriptionCancellationsRepo;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private SubscriptionMSServiceImpl subscriptionMSService;
	
	@BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
	void testGetSubscriptionPlans() {
		List<SubscriptionPlans> subscriptionPlans = new ArrayList<>();
        subscriptionPlans.add(new SubscriptionPlans());
        subscriptionPlans.add(new SubscriptionPlans());
        when(subscriptionPlansRepo.findAll()).thenReturn(subscriptionPlans);

        List<SubscriptionPlansDTO> result = subscriptionMSService.getSubscriptionPlans();

        verify(subscriptionPlansRepo, times(1)).findAll();

        assertEquals(subscriptionPlans.size(), result.size());
	}	
	
	@Test
	void testPurchaseSubscription() {
	    NewUserSubscriptionDTO userSubscription = new NewUserSubscriptionDTO();
	    userSubscription.setValidityDuration("6");
	    userSubscription.setPlanID(2);
	    userSubscription.setPaymentMode("Card");
	    userSubscription.setUserName("testUser");

	    SubscriptionPlans requestedSubscription = new SubscriptionPlans();
	    requestedSubscription.setPricePerMonth(10);
	    requestedSubscription.setPlanName("Basic");
	    when(subscriptionPlansRepo.findById(2)).thenReturn(Optional.of(requestedSubscription));

	    double finalAmount = 57.0;
	    when(userSubscriptionsRepo.save(any(UserSubscriptions.class))).thenAnswer(invocation -> {
	        UserSubscriptions savedSubscription = invocation.getArgument(0);
	        savedSubscription.setAmountPaid(finalAmount);
	        return savedSubscription;
	    });

	    UserSubscriptions result = this.subscriptionMSService.purchaseSubscription(userSubscription);

	    verify(subscriptionPlansRepo, times(3)).findById(2);
	    verify(userSubscriptionsRepo, times(1)).save(any(UserSubscriptions.class));

	    assertNotNull(result);
	    assertEquals(requestedSubscription, result.getPlanID());
	    assertEquals(userSubscription.getPaymentMode(), result.getPaymentMode());
	    assertEquals(userSubscription.getUserName(), result.getUserName());
	    assertEquals(finalAmount, result.getAmountPaid());
	    assertEquals(Date.valueOf(LocalDate.now()), result.getSubscriptionStartDate());
	    assertEquals(Date.valueOf(LocalDate.now().plusMonths(6)), result.getSubscriptionEndDate());
	    assertEquals("New", result.getSubscriptionStatus());
	    assertNull(result.getSubscriptionCancel());
	}

	@Test
	void testRenewSubscription() {
		int subscriptionID = 1;
        RenewUserSubscriptionDTO userSubscription = new RenewUserSubscriptionDTO();
        userSubscription.setValidityDuration("6");
        userSubscription.setPaymentMode("NetBanking");

        SubscriptionPlans subscriptionPlan = new SubscriptionPlans();
        subscriptionPlan.setPlanID(1);
        subscriptionPlan.setPricePerMonth(25);
        subscriptionPlan.setPlanName("Pro");

        UserSubscriptions existingSubscription = new UserSubscriptions();
        existingSubscription.setPlanID(subscriptionPlan);
        existingSubscription.setAmountPaid(25.0);
        existingSubscription.setSubscriptionStartDate(Date.valueOf(LocalDate.now()));
        existingSubscription.setSubscriptionEndDate(Date.valueOf(LocalDate.now().plusMonths(6)));
        existingSubscription.setSubscriptionStatus("New");

        when(userSubscriptionsRepo.findById(subscriptionID)).thenReturn(Optional.of(existingSubscription));
        when(subscriptionPlansRepo.findById(subscriptionPlan.getPlanID())).thenReturn(Optional.of(subscriptionPlan));
        when(userSubscriptionsRepo.save(any(UserSubscriptions.class))).thenReturn(existingSubscription);

        UserSubscriptions renewedSubscription = this.subscriptionMSService.renewSubscription(subscriptionID, userSubscription);

        verify(userSubscriptionsRepo, times(1)).findById(subscriptionID);
        verify(subscriptionPlansRepo, times(2)).findById(subscriptionPlan.getPlanID());
        verify(userSubscriptionsRepo, times(1)).save(any(UserSubscriptions.class));

        assertEquals(userSubscription.getPaymentMode(), renewedSubscription.getPaymentMode());
        assertEquals("Renewed", renewedSubscription.getSubscriptionStatus());
        assertNull(renewedSubscription.getSubscriptionCancel());
	}

	@Test
	void testCancelSubcription() {
		int subscriptionID = 1;
        CancelUserSubscriptionDTO userSubscription = new CancelUserSubscriptionDTO();
        userSubscription.setCancellationReason("No longer needed");

        UserSubscriptions existingSubscription = new UserSubscriptions();
        existingSubscription.setSubscriptionID(subscriptionID);
        existingSubscription.setSubscriptionStatus("Cancelled");

        SubscriptionCancellations cancellation = new SubscriptionCancellations();
        cancellation.setCancellationDate(Date.valueOf(LocalDate.now()));
        cancellation.setCancellationReason(userSubscription.getCancellationReason());

        when(userSubscriptionsRepo.findById(subscriptionID)).thenReturn(Optional.of(existingSubscription));
        when(subscriptionCancellationsRepo.save(any(SubscriptionCancellations.class))).thenReturn(cancellation);
        when(userSubscriptionsRepo.save(existingSubscription)).thenReturn(existingSubscription);

        SubscriptionCancellations cancelledSubscription = this.subscriptionMSService.cancelSubscription(subscriptionID, userSubscription);

        verify(userSubscriptionsRepo, times(1)).findById(subscriptionID);
        verify(subscriptionCancellationsRepo, times(1)).save(any(SubscriptionCancellations.class));
        verify(userSubscriptionsRepo, times(1)).save(existingSubscription);

        assertEquals("Cancelled", existingSubscription.getSubscriptionStatus());
        assertNotNull(existingSubscription.getSubscriptionCancel());
        assertEquals(cancellation.getCancellationReason(), existingSubscription.getSubscriptionCancel().getCancellationReason());
        assertNotNull(cancelledSubscription);
        
	}

	@Test
	void testGetSubscriptionPlanByUsername() {
		UserSubscriptions userSubs = new UserSubscriptions();
		when(this.userSubscriptionsRepo.findSubscriptionByUsername("sundhar_sg")).thenReturn(userSubs);
		
		UserSubscriptionsDTO userSubsByService = this.subscriptionMSService.getSubscriptionPlanByUsername("sundhar_sg");
		UserSubscriptionsDTO userSubsByRepo = this.modelMapper.map(userSubs, UserSubscriptionsDTO.class);
		
		verify(this.userSubscriptionsRepo).findSubscriptionByUsername("sundhar_sg");
		assertEquals(userSubsByRepo, userSubsByService);
		
	}

}
