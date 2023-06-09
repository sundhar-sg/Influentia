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
	
	@PutMapping("/cancel-post")
	public ResponseEntity<String> cancelScheduledPost(@RequestBody CancelDTO cancelData) {
		int numberofUpdations = cmService.cancelScheduledPost(cancelData.getUsername(), cancelData.getPostID());
		if(numberofUpdations > 0)
			return ResponseEntity.status(HttpStatus.OK).body("Successfully cancelled a post :)");
		log.error("Cancelling the user post is not possible as there is no posts with username: {} and post ID: {}", cancelData.getUsername(), cancelData.getPostID());
		throw new NoSuchElementException("Failed in cancelling the user post with username: " +cancelData.getUsername()+ " and ID: " +cancelData.getPostID());
	}
	
	@GetMapping("/post-analytics/{username}")
	public ResponseEntity<?> getRegisteredAccountsOfUser(@PathVariable("username") String username) {
		List<String> finalResult = cmService.fetchDistinctSocialAccountsofUser(username);
		if(finalResult.size() >= 1)
			return ResponseEntity.status(HttpStatus.OK).body(finalResult);
		log.error("There are no social accounts registered for the logged in user");
		throw new NoSuchElementException("There are no social accounts registered for the logged in user: " +username);
	}
	
	@GetMapping("/post-analytics/monthly/{username}/{month}/{year}/{socialAccountType}/{postType}")
	public ResponseEntity<?> numberOfUserPostsBasedOnMonthlyInsights(@PathVariable("username") String username, @PathVariable("socialAccountType") String socialAccountType, @PathVariable("month") String month, @PathVariable("year") int year, @PathVariable("postType") String postType) {
		return ResponseEntity.status(HttpStatus.OK).body(cmService.postAnalyticsBasedOnMonth(username, month, year, socialAccountType, postType));
	}
	
	@GetMapping("/post-analytics/quarterly/{username}/{quarterType}/{year}/{socialAccountType}/{postType}")
	public ResponseEntity<?> numberOfUserPostsBasedOnQuarterInsights(@PathVariable("username") String username, @PathVariable("quarterType") String quarterType, @PathVariable("year") int year, @PathVariable("socialAccountType") String socialAccountType, @PathVariable("postType") String postType) {
		return ResponseEntity.status(HttpStatus.OK).body(cmService.postAnalyticsBasedOnQuarter(username, year, quarterType, socialAccountType, postType));
	}
	
	@GetMapping("/post-analytics/half-yearly/{username}/{halfYearlyType}/{year}/{socialAccountType}/{postType}")
	public ResponseEntity<?> numberOfUserPostsBasedOnHalfYearlyInsights(@PathVariable("username") String username, @PathVariable("halfYearlyType") String halfYearlyType, @PathVariable("year") int year, @PathVariable("socialAccountType") String socialAccountType, @PathVariable("postType") String postType) {
		return ResponseEntity.status(HttpStatus.OK).body(cmService.postAnalyticsBasedOnSemiAnnual(username, year, halfYearlyType, socialAccountType, postType));
	}
	
	@GetMapping("/post-analytics/yearly/{username}/{year}/{socialAccountType}/{postType}")
	public ResponseEntity<?> numberOfUserPostsBasedOnYearlyInsights(@PathVariable("username") String username, @PathVariable("year") int year, @PathVariable("socialAccountType") String socialAccountType, @PathVariable("postType") String postType) {
		return ResponseEntity.status(HttpStatus.OK).body(cmService.postAnalyticsBasedOnYear(username, year, socialAccountType, postType));
	}
	
	@GetMapping("/post-analytics/custom/{username}/{startDate}/{endDate}/{socialAccountType}/{postType}")
	public ResponseEntity<?> numberOfUserPostsBasedOnCustomInsights(@PathVariable("username") String username, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, @PathVariable("socialAccountType") String socialAccountType, @PathVariable("postType") String postType) {
		Date startDateConvert = Date.from(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date endDateConvert = Date.from(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant());
		return ResponseEntity.status(HttpStatus.OK).body(cmService.postAnalyticsBasedOnCustomDates(startDateConvert, endDateConvert, socialAccountType, username, postType));
	}
}