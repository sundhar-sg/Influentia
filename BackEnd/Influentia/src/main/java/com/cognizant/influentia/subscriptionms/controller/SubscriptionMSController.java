package com.cognizant.influentia.subscriptionms.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.cognizant.influentia.subscriptionms.dto.CancelUserSubscriptionDTO;
import com.cognizant.influentia.subscriptionms.dto.NewUserSubscriptionDTO;
import com.cognizant.influentia.subscriptionms.dto.RenewUserSubscriptionDTO;
import com.cognizant.influentia.subscriptionms.dto.SubscriptionPlansDTO;
import com.cognizant.influentia.subscriptionms.dto.UserSubscriptionsDTO;
import com.cognizant.influentia.subscriptionms.service.SubscriptionMSService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/subscriptions")
@Validated
@Slf4j
public class SubscriptionMSController {
	
	@Autowired
	SubscriptionMSService subscriptionMSService;

	@GetMapping("/plan")
	public ResponseEntity<List<SubscriptionPlansDTO>> fetchSubscriptionPlans() {
		log.info("Successfully fetched all the available Subscription Plans");
		return new ResponseEntity<>(this.subscriptionMSService.getSubscriptionPlans(), HttpStatus.FOUND);
	}
	
	@PostMapping("/purchase")
	public ResponseEntity<Void> newUserSubscription(@RequestBody @Valid NewUserSubscriptionDTO newSubscription) {
		this.subscriptionMSService.purchaseSubscription(newSubscription);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{subscriptionid}/renew")
	public ResponseEntity<Void> renewUserSubscription(@PathVariable("subscriptionid") int subscriptionID, @RequestBody @Valid RenewUserSubscriptionDTO renewSubscription) {
		try {
			this.subscriptionMSService.renewSubscription(subscriptionID, renewSubscription);
			return ResponseEntity.ok().build();
		} catch (NoSuchElementException ex) {
			// TODO: handle exception
			log.error("There is no user subscription associated with the provided subscription ID, Renewal not possible. Purchase a new Subscription instead");
			throw new NoSuchElementException("There is no user subscription associated with the provided subscription ID: " +subscriptionID+ ". Purchase a new Subscription instead");
		}
	}
	
	@PutMapping("/{subscriptionid}/cancel")
	public ResponseEntity<Void> cancelUserSubscription(@PathVariable("subscriptionid") int subscriptionID, @RequestBody @Valid CancelUserSubscriptionDTO cancelSubscription) {
		try {
			this.subscriptionMSService.cancelSubcription(subscriptionID, cancelSubscription);
			return ResponseEntity.ok().build();
		} catch (NoSuchElementException ex) {
			// TODO: handle exception
			log.error("There is no user subscription associated with the provided subscription ID, Cancellation not possible.");
			throw new NoSuchElementException("There is no user subscription associated with the provided subscription ID: " +subscriptionID+ ".");
		}
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<UserSubscriptionsDTO> getSubscriptionByUser(@PathVariable("username") String username) {
		try {
			return new ResponseEntity<>(this.subscriptionMSService.getSubscriptionPlanByUsername(username), HttpStatus.FOUND);
		} catch (NoSuchElementException ex) {
			log.error("There is no user subscription associated with the provided username");
			throw new NoSuchElementException("There is no user subscription associated with the provided username: " +username);
		}
	}
}