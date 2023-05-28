package com.cognizant.influentia.contentms.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cognizant.influentia.contentms.dto.*;
import com.cognizant.influentia.contentms.entity.*;
import com.cognizant.influentia.contentms.service.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/content")
@Validated
public class ContentMSController {
	
	@Autowired
	ContentManagementService cmService;

	@GetMapping("/getSubsPlanLimits/{SubscriberID}")
	public SubscriptionPlanLimitsDTO fetchSubsPLById(@PathVariable("SubscriberID") int id) {
		return cmService.getPlan(id);
	}
	
	@PostMapping("/add")
	public ResponseEntity<UserPosts> addNewUserPosts(@RequestBody @Valid UserPostsDTO userPostDTO) {
		UserPosts createdPost = cmService.addNewPost(userPostDTO);
		return new ResponseEntity<UserPosts>(createdPost, HttpStatus.CREATED);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<List<UserPostsDTO>> getUserPostsOfUser(@PathVariable("username") String username) {
		return new ResponseEntity<List<UserPostsDTO>>(cmService.getUserPostsByUserName(username), HttpStatus.FOUND);
	}
	
	@PutMapping("/{Username}/cancel/{PostID}")
	public ResponseEntity<String> cancelScheduledPost(@PathVariable("Username") String username, @PathVariable("PostID") int id) {
		int numberOfUpdations = cmService.cancelScheduledPost(username, id);
		if(numberOfUpdations > 0)
			return new ResponseEntity<String>("Successfully cancelled a post :)", HttpStatus.ACCEPTED);
		return new ResponseEntity<String>("Failed in cancelling the post :(", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/analytics")
	public ResponseEntity<List<UserPostsDTO>> getUserPostsBasedOnAnalytics(@RequestParam Map<String, String> parameterList) {
		List<UserPostsDTO> finalResult = cmService.filteredUserPosts(parameterList);
		if(finalResult.size() >= 1)
			return new ResponseEntity<List<UserPostsDTO>>(finalResult, HttpStatus.FOUND);
		return new ResponseEntity<List<UserPostsDTO>>(HttpStatus.NOT_FOUND);
	}
}