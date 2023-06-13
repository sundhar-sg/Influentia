package com.cognizant.influentia.contentms.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cognizant.influentia.contentms.dto.*;
import com.cognizant.influentia.contentms.service.*;
import com.cognizant.influentia.exception.GlobalExceptionHandler;
import com.cognizant.influentia.exception.ResourceQuotaExceededException;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/content")
@Validated
@Slf4j
public class ContentMSController {
	
	@Autowired
	ContentMSService cmService;
	
	@Autowired
	GlobalExceptionHandler excHandler;

	@GetMapping("/getSubsPlanLimits/{SubscriberID}")
	public SubscriptionPlanLimitsDTO fetchSubsPLById(@PathVariable("SubscriberID") int id) {
		try {
			return cmService.getPlan(id);
		} catch(NoSuchElementException ex) {
			log.error("Subscription plans with their limits for the provided ID: {} is not found", id);
			throw new NoSuchElementException("There is no subscription plan with limits found for the provided ID: " +id);
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addNewUserPosts(@RequestBody @Valid UserPostsDTO userPostDTO) throws ResourceQuotaExceededException {
		cmService.addNewPost(userPostDTO);
		return ResponseEntity.status(HttpStatus.OK).body("Successfully Created the Post");
	}
	
	@GetMapping(value = "/{username}/all-posts")
	public ResponseEntity<?> getUserPostsOfUser(@PathVariable("username") String username) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(cmService.getUserPostsByUserName(username));
		} catch(NoSuchElementException ex) {
			log.error("UserPosts with the specified username: {} are not found", username);
			throw new NoSuchElementException("There are no user posts found with the given username: " +username);
		}
	}
	
	@PutMapping("/{Username}/cancel/{PostID}")
	public ResponseEntity<String> cancelScheduledPost(@PathVariable("Username") String username, @PathVariable("PostID") int id) {
		int numberofUpdations = cmService.cancelScheduledPost(username, id);
		if(numberofUpdations > 0)
			return new ResponseEntity<String>("Successfully cancelled a post :)", HttpStatus.OK);
		log.error("Cancelling the user post is not possible as there is no posts with username: {} and post ID: {}", username, id);
		throw new NoSuchElementException("Failed in cancelling the user post with username: " +username+ " and ID: " +id);
	}
	
	@GetMapping("/post-analytics/{username}")
	public ResponseEntity<?> getRegisteredAccountsOfUser(@PathVariable("username") String username) {
		List<String> finalResult = cmService.fetchDistinctSocialAccountsofUser(username);
		if(finalResult.size() >= 1)
			return ResponseEntity.status(HttpStatus.FOUND).body(finalResult);
		log.error("There are no social accounts registered for the logged in user");
		throw new NoSuchElementException("There are no social accounts registered for the logged in user: " +username);
	}
	
	@GetMapping("/post-analytics/monthly/{username}/{month}/{year}/{socialAccountType}")
	public ResponseEntity<?> numberOfUserPostsBasedOnMonthlyInsights(@PathVariable("username") String username, @PathVariable("socialAccountType") String socialAccountType, @PathVariable("month") String month, @PathVariable("year") int year) {
		int result = cmService.postAnalyticsBasedOnMonth(username, month, year, socialAccountType);
		if(result >= 1)
			return ResponseEntity.status(HttpStatus.FOUND).body(result);
		log.error("There are no user posts found for the specified insights");
		throw new NoSuchElementException("There are no user posts found for the specified insights");
	}
	
	@GetMapping("/post-analytics/quarterly/{username}/{quarterType}/{year}/{socialAccountType}")
	public ResponseEntity<?> numberOfUserPostsBasedOnQuarterInsights(@PathVariable("username") String username, @PathVariable("quarterType") String quarterType, @PathVariable("year") int year, @PathVariable("socialAccountType") String socialAccountType) {
		int result = cmService.postAnalyticsBasedOnQuarter(username, year, quarterType, socialAccountType);
		if(result >= 1)
			return ResponseEntity.status(HttpStatus.FOUND).body(result);
		log.error("There are no user posts found for the specified insights");
		throw new NoSuchElementException("There are no user posts found for the specified insights");
	}
	
	@GetMapping("/post-analytics/half-yearly/{username}/{halfYearlyType}/{year}/{socialAccountType}")
	public ResponseEntity<?> numberOfUserPostsBasedOnHalfYearlyInsights(@PathVariable("username") String username, @PathVariable("halfYearlyType") String halfYearlyType, @PathVariable("year") int year, @PathVariable("socialAccountType") String socialAccountType) {
		int result = cmService.postAnalyticsBasedOnSemiAnnual(username, year, halfYearlyType, socialAccountType);
		if(result >= 1)
			return ResponseEntity.status(HttpStatus.FOUND).body(result);
		log.error("There are no user posts found for the specified insights");
		throw new NoSuchElementException("There are no user posts found for the specified insights");
	}
	
	@GetMapping("/post-analytics/yearly/{username}/{year}/{socialAccountType}")
	public ResponseEntity<?> numberOfUserPostsBasedOnYearlyInsights(@PathVariable("username") String username, @PathVariable("year") int year, @PathVariable("socialAccountType") String socialAccountType) {
		int result = cmService.postAnalyticsBasedOnYear(username, year, socialAccountType);
		if(result >= 1)
			return ResponseEntity.status(HttpStatus.FOUND).body(result);
		log.error("There are no user posts found for the specified insights");
		throw new NoSuchElementException("There are no user posts found for the specified insights");
	}
	
	@GetMapping("/post-analytics/custom/{username}/{startDate}/{endDate}/{socialAccountType}")
	public ResponseEntity<?> numberOfUserPostsBasedOnCustomInsights(@PathVariable("username") String username, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, @PathVariable("socialAccountType") String socialAccountType) {
		Date startDateConvert = Date.from(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date endDateConvert = Date.from(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant());
		int result = cmService.postAnalyticsBasedOnCustomDates(startDateConvert, endDateConvert, socialAccountType, username);
		if(result >= 1) 
			return ResponseEntity.status(HttpStatus.FOUND).body(result);
		log.error("There are no user posts found for the specified insights");
		throw new NoSuchElementException("There are no user posts found for the specified insights");
	}
}