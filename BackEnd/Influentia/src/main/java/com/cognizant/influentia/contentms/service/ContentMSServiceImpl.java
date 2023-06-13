package com.cognizant.influentia.contentms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

import com.cognizant.influentia.contentms.dto.*;
import com.cognizant.influentia.contentms.entity.*;
import com.cognizant.influentia.contentms.repository.*;
import com.cognizant.influentia.exception.ResourceQuotaExceededException;
import com.cognizant.influentia.subscriptionms.entity.UserSubscriptions;
import com.cognizant.influentia.subscriptionms.repository.UserSubscriptionsRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContentMSServiceImpl implements ContentMSService {
	
	@Autowired
	private UserPostsRepository upRepo;
	
	@Autowired
	private SubscriptionPlanLimitsRepository subsPLRepo;
	
	@Autowired
	private UserSubscriptionsRepo userSubscriptionsRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	Scanner sc = new Scanner(System.in);
	
	@Override
	public SubscriptionPlanLimitsDTO getPlan(Integer id) {
		SubscriptionPlanLimits subsPL = subsPLRepo.findById(id).get();
		SubscriptionPlanLimitsDTO subsPLDTO = this.modelMapper.map(subsPL, SubscriptionPlanLimitsDTO.class);
		return subsPLDTO;
	}
	
	@Override
	public UserPosts addNewPost(UserPostsDTO userPostDTO) throws ResourceQuotaExceededException {
		String username = userPostDTO.getUsername();
		userPostDTO.setPostedOn(new Date());
		LocalDate publishedOnDate = userPostDTO.getPublishedOnDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		UserSubscriptions subscriptionPlan = this.userSubscriptionsRepo.findSubscriptionByUsername(username);
		if(subscriptionPlan == null) {
			log.error("There is no subscription plan associated with the given username. First, Get a subscription plan instead");
			throw new IllegalArgumentException("You haven't any active subscription for your account. Get a subscription plan instead :)");
		}
		int numberOfPosts = this.upRepo.findNumberOfPostsBasedOnUserName(username, publishedOnDate.getMonthValue());
		System.out.println("Number of Posts: " + String.valueOf(numberOfPosts));
		if((numberOfPosts >= 5) && (subscriptionPlan.getPlanID().getPlanName().equalsIgnoreCase("Basic"))) {
			log.error("You have reached the posts limit for your existing subscription. Wait till next month or upgrade your subscription");
			throw new ResourceQuotaExceededException("You have reached the posts limit for your existing subscription. Wait till next month or upgrade your subscription");
		} else if((numberOfPosts >= 150) && (subscriptionPlan.getPlanID().getPlanName().equalsIgnoreCase("Pro"))) {
			log.error("You have reached the posts limit for your existing subscription. Wait till next month for adding new posts");
			throw new ResourceQuotaExceededException("You have reached the posts limit for your existing subscription. Wait till next month for adding new posts");
		}
		LocalTime publishedOnTime = LocalDateTime.ofInstant(userPostDTO.getPublishedOnTime().toInstant(), ZoneId.systemDefault()).toLocalTime();
		UserPosts userPost = this.modelMapper.map(userPostDTO, UserPosts.class);
		userPost.setPublishedOnDate(publishedOnDate);
		userPost.setPublishedOnTime(publishedOnTime);
		return upRepo.save(userPost);
	}
	
	@Override
	public int cancelScheduledPost(String username, int id) {
		return upRepo.cancelScheduledPost(username, id);
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
			UserPostsDTO userPostDTO = this.modelMapper.map(userPost, UserPostsDTO.class);
			userPostDTO.setPublishedOnDate(Date.from(userPost.getPublishedOnDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			userPostDTO.setPublishedOnTime(Date.from(LocalDateTime.of(userPost.getPublishedOnDate(), userPost.getPublishedOnTime()).atZone(ZoneId.systemDefault()).toInstant()));
			log.debug("Mapped UserPostDTO = {}", userPostDTO);
			finalResult.add(userPostDTO);
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

	@Override
	public List<String> fetchDistinctSocialAccountsofUser(String username) {
		// TODO Auto-generated method stub
		return upRepo.numberOfUniqueAccountsForUser(username);
	}

	@Override
	public int postAnalyticsBasedOnMonth(String username, String month, int year, String socialAccountType) {
		// TODO Auto-generated method stub
		return upRepo.findMonthlyInsightsByPublishedOnDate(month, year, socialAccountType, username);
	}

	@Override
	public int postAnalyticsBasedOnQuarter(String username, int year, String quarterType, String socialAccountType) {
		// TODO Auto-generated method stub
		if(quarterType.equalsIgnoreCase("1st Quarter"))
			return upRepo.findQuarterlyInsightsByPublishedOnDate(1, 3, year, socialAccountType, username);
		else if(quarterType.equalsIgnoreCase("2nd Quarter"))
			return upRepo.findQuarterlyInsightsByPublishedOnDate(4, 6, year, socialAccountType, username);
		else if(quarterType.equalsIgnoreCase("3rd Quarter"))
			return upRepo.findQuarterlyInsightsByPublishedOnDate(7, 9, year, socialAccountType, username);
		else if(quarterType.equalsIgnoreCase("4th Quarter"))
			return upRepo.findQuarterlyInsightsByPublishedOnDate(10, 12, year, socialAccountType, username);
		return 0;
	}

	@Override
	public int postAnalyticsBasedOnSemiAnnual(String username, int year, String semiAnnualType, String socialAccountType) {
		// TODO Auto-generated method stub
		if(semiAnnualType.equalsIgnoreCase("1st Semi Annual"))
			return upRepo.findHalfYearlyInsightsByPublishedOnDate(1, 6, year, socialAccountType, username);
		else if(semiAnnualType.equalsIgnoreCase("2nd Semi Annual"))
			return upRepo.findHalfYearlyInsightsByPublishedOnDate(7, 12, year, socialAccountType, username);
		return 0;
	}

	@Override
	public int postAnalyticsBasedOnYear(String username, int year, String socialAccountType) {
		// TODO Auto-generated method stub
		return upRepo.findYearlyInsightsByPublishedOnDate(year, socialAccountType, username);
	}

	@Override
	public int postAnalyticsBasedOnCustomDates(Date startDate, Date endDate, String socialAccountType, String username) {
		// TODO Auto-generated method stub
		return upRepo.findCustomInsightsByPublishedOnDate(username, startDate, endDate, socialAccountType);
	}
}