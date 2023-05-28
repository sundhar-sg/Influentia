package com.cognizant.influentia.contentms.service;

import java.util.*;

import com.cognizant.influentia.contentms.dto.*;

// import org.springframework.web.multipart.MultipartFile;

import com.cognizant.influentia.contentms.entity.UserPosts;

public interface ContentManagementService {
	
	SubscriptionPlanLimitsDTO getPlan(Integer id);
	
	UserPostsDTO getUserPostById(Integer id);

	UserPosts addNewPost(UserPostsDTO userPostDTO);

	int cancelScheduledPost(String username, int id);

	List<UserPostsDTO> filteredUserPosts(Map<String, String> insightType);
	
	List<UserPostsDTO> getUserPostsByUserName(String username);

//	boolean fileValidation(MultipartFile file, String fileType);

}