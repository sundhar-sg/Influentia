package com.cognizant.influentia.contentms.service;

import java.time.LocalDateTime;
import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

import com.cognizant.influentia.contentms.dto.*;
import com.cognizant.influentia.contentms.entity.*;
import com.cognizant.influentia.contentms.repository.*;

@Service
public class ContentManagementServiceImpl implements ContentManagementService {
	
	@Autowired
	private UserPostsRepository upRepo;
	
	@Autowired
	private SubscriptionPlanLimitsRepository subsPLRepo;
	
	private ModelMapper modelMapper=new ModelMapper();
	
	Scanner sc = new Scanner(System.in);
	
	@Override
	public SubscriptionPlanLimitsDTO getPlan(Integer id) {
		SubscriptionPlanLimits subsPL = subsPLRepo.findById(id).get();
		SubscriptionPlanLimitsDTO subsPLDTO = this.modelMapper.map(subsPL, SubscriptionPlanLimitsDTO.class);
		return subsPLDTO;
	}
	
	@Override
	public UserPosts addNewPost(UserPostsDTO userPostDTO) {
		LocalDateTime publishedOnTimestamp = userPostDTO.getPublishedOnTimestamp();
		userPostDTO.setPublishedOnTime(publishedOnTimestamp.toLocalTime());
		UserPosts userPost = this.modelMapper.map(userPostDTO, UserPosts.class);
		return upRepo.save(userPost);
	}
	
	@Override
	public int cancelScheduledPost(String username, int id) {
		return upRepo.cancelScheduledPost(username, id);
	}
	
	@Override
	public List<UserPostsDTO> filteredUserPosts(Map<String, String> insightParameters) {
		List<UserPosts> finalResult = null;
		if(insightParameters.get("insightType").equalsIgnoreCase("month")) 
			finalResult = upRepo.findMonthlyInsightsByPublishedOnDate(insightParameters.get("month"), Integer.parseInt(insightParameters.get("year")));
		else if(insightParameters.get("insightType").equalsIgnoreCase("quarter")) {
			switch(Integer.parseInt(insightParameters.get("quarterType"))) {
				case 1: finalResult = upRepo.findQuarterlyInsightsByPublishedOnDate(1, 3, Integer.parseInt(insightParameters.get("year")));
						break;
				case 2: finalResult = upRepo.findQuarterlyInsightsByPublishedOnDate(4, 6, Integer.parseInt(insightParameters.get("year")));
						break;
				case 3: finalResult = upRepo.findQuarterlyInsightsByPublishedOnDate(7, 9, Integer.parseInt(insightParameters.get("year")));
						break;
				case 4: finalResult = upRepo.findQuarterlyInsightsByPublishedOnDate(10, 12, Integer.parseInt(insightParameters.get("year")));
						break;
			}
		}
		else if(insightParameters.get("insightType").equalsIgnoreCase("semiannual")) {
			switch(Integer.parseInt(insightParameters.get("semiAnnualType"))) {
				case 1: finalResult = upRepo.findHalfYearlyInsightsByPublishedOnDate(1, 6, Integer.parseInt(insightParameters.get("year")));
						break;
				case 2: finalResult = upRepo.findHalfYearlyInsightsByPublishedOnDate(7, 12, Integer.parseInt(insightParameters.get("year")));
						break;
			}
		}
		else if(insightParameters.get("insightType").equalsIgnoreCase("yearly")) 
			finalResult = upRepo.findYearlyInsightsByPublishedOnDate(Integer.parseInt(insightParameters.get("year")));
		List<UserPostsDTO> finalResultDTO = new ArrayList<>();
		for(UserPosts userPost : finalResult) {
			finalResultDTO.add(this.modelMapper.map(userPost, UserPostsDTO.class));
		}
		return finalResultDTO;
	}
	
//	@Override
//	public boolean fileValidation(MultipartFile file, String fileType) {
//		boolean fileCheck = false;
//		long fileSize = 0;
//		if(fileType.equalsIgnoreCase("Image")) {
//			fileSize = file.getSize();
//			if(fileSize <= (1*1024*1024))
//				fileCheck = true;
//			else
//				fileCheck = false;
//		}
//		else if(fileType.equalsIgnoreCase("Video")) {
//			fileSize = file.getSize();
//			if(fileSize <= (10*1024*1024))
//				fileCheck = true;
//			else
//				fileCheck = false;
//		}
//		return fileCheck;
//	}

	@Override
	public List<UserPostsDTO> getUserPostsByUserName(String username) {
		// TODO Auto-generated method stub
		List<UserPosts> initialResult = upRepo.findAllUserPostsByUserName(username);
		List<UserPostsDTO> finalResult = new ArrayList<>();
		for(UserPosts userPost : initialResult) {
			finalResult.add(this.modelMapper.map(userPost, UserPostsDTO.class));
		}
		return finalResult;
	}

	@Override
	public UserPostsDTO getUserPostById(Integer id) {
		// TODO Auto-generated method stub
		UserPosts resultUserPost = upRepo.findById(id).get();
		UserPostsDTO finalResult = this.modelMapper.map(resultUserPost, UserPostsDTO.class);
		return finalResult;
	}
}