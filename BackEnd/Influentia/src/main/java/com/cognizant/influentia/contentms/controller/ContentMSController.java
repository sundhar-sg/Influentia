package com.cognizant.influentia.contentms.controller;

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
		return new ResponseEntity<String>("Successfully Created the Post", HttpStatus.OK);
	}
	
	@GetMapping("/{username}/all-posts")
	public ResponseEntity<List<UserPostsDTO>> getUserPostsOfUser(@PathVariable("username") String username) {
		try {
			return new ResponseEntity<List<UserPostsDTO>>(cmService.getUserPostsByUserName(username), HttpStatus.FOUND);
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
	
	@GetMapping("/post-analytics")
	public ResponseEntity<List<UserPostsDTO>> getUserPostsBasedOnAnalytics(@RequestParam Map<String, String> parameterList) {
		List<UserPostsDTO> finalResult = cmService.filteredUserPosts(parameterList);
		if(finalResult.size() >= 1)
			return new ResponseEntity<List<UserPostsDTO>>(finalResult, HttpStatus.FOUND);
		log.error("There are no user posts available for the logged in user with the specified analytics :(");
		throw new NoSuchElementException("No User Posts found for the logged in user with the specified analytics type: " +parameterList.get("insightType"));
	}
}