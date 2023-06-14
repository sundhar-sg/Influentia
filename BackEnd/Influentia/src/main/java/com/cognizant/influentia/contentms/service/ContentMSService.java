package com.cognizant.influentia.contentms.service;

import java.util.*;

import com.cognizant.influentia.contentms.dto.*;

// import org.springframework.web.multipart.MultipartFile;

import com.cognizant.influentia.contentms.entity.UserPosts;
import com.cognizant.influentia.exception.ResourceQuotaExceededException;

public interface ContentMSService {
	
	SubscriptionPlanLimitsDTO getPlan(Integer id);
	
	UserPostsDTO getUserPostById(Integer id);

	UserPosts addNewPost(UserPostsDTO userPostDTO) throws ResourceQuotaExceededException;

	int cancelScheduledPost(String username, int id);

	List<String> fetchDistinctSocialAccountsofUser(String username);
	
	int postAnalyticsBasedOnMonth(String username, String month, int year, String socialAccountType, String postType);
	
	int postAnalyticsBasedOnQuarter(String username, int year, String quarterType, String socialAccountType, String postType);
	
	int postAnalyticsBasedOnSemiAnnual(String username, int year, String semiAnnualType, String socialAccountType, String postType);
	
	int postAnalyticsBasedOnYear(String username, int year, String socialAccountType, String postType);
	
	int postAnalyticsBasedOnCustomDates(Date startDate, Date endDate, String socialAccountType, String username, String postType);
	
	List<UserPostsDTO> getUserPostsByUserName(String username);

//	boolean fileValidation(MultipartFile file, String fileType);

}