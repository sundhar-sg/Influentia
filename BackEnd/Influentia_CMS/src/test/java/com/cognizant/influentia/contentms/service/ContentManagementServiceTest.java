package com.cognizant.influentia.contentms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cognizant.influentia.contentms.dto.SubscriptionPlanLimitsDTO;
import com.cognizant.influentia.contentms.dto.UserPostsDTO;
import com.cognizant.influentia.contentms.entity.*;
import com.cognizant.influentia.contentms.repository.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class ContentManagementServiceTest {
	
	@Mock
	UserPostsRepository upRepo;
	
	@Mock
	SubscriptionPlanLimitsRepository subsPLRepo;

	private ModelMapper modelMapper =new ModelMapper();
	
	@InjectMocks
	ContentManagementServiceImpl cmService;
	
	Date currentDate = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
/*
 * org.opentest4j.AssertionFailedError: expected: 
 * <[UserPosts [id=0, postedOn=null, isScheduledPost=false, publishedOnDate=null, publishedOnTime=null, postType=null, postContextText=null, postAttachmentURL=null, postStatus=null, username=null, socialNetworkType=null]]> but was: 
 * <[UserPostsDTO(id=0, postedOn=null, isScheduledPost=false, publishedOnDate=null, publishedOnTime=null, postType=null, postContentText=null, postAttachmentURL=null, postStatus=null, username=null, socialNetworkType=null)]>

 */
	@Test
	void testGetPlan() {
		SubscriptionPlanLimits subscription = new SubscriptionPlanLimits("Pro", 150);
		
		when(subsPLRepo.findById(1).get()).thenReturn(subscription);
		SubscriptionPlanLimitsDTO subsByService = this.cmService.getPlan(1);
		SubscriptionPlanLimitsDTO subsByRepo = this.modelMapper.map(subscription, SubscriptionPlanLimitsDTO.class);
		verify(this.subsPLRepo).findById(1).get();
		assertEquals(subsByRepo, subsByService);
//		SubscriptionPlanLimits subsPL = cmService.getPlan(1);
//		assertThat(subsPL.getUserName()).isEqualTo("sundhar_sg");
//		assertThat(subsPL.getSubscriptionPlanName()).isEqualTo("Pro");
//		assertThat(subsPL.getMonthlyScheduledPostLimit()).isEqualTo(150);
	}

	@Test
	void testAddNewPost() throws ParseException {
		UserPosts userPost = new UserPosts(currentDate, true, sdf.parse("27-01-2024"), stf.parse("00:00:00"), "Video", "Wishing you a very happy birthday my mentor", "https://facebook.com/sundhar_sg/post/35378hjdbvs", "Scheduled", "sundhar_sg", "Facebook");
		when(this.upRepo.save(any())).thenReturn(userPost);
		UserPosts serviceUP = this.cmService.addNewPost(userPost);
		assertEquals(serviceUP, userPost);
	}

	@Test
	void testCancelScheduledPost() throws ParseException {
		UserPosts userPost = new UserPosts(currentDate, true, sdf.parse("28-01-2024"), stf.parse("00:00:00"), "Video", "Wishing you a very happy birthday my mentor", "https://instagram.com/sundhar_sg/post/35378hjdbvs", "Scheduled", "kishore_k", "Instagram");
		int noOfUpdateRequired = 1;
		when(this.upRepo.cancelScheduledPost(any(String.class), any(Integer.class))).thenReturn(noOfUpdateRequired);
		int noOfUpdations = this.cmService.cancelScheduledPost(userPost.getUsername(), userPost.getId());
		assertEquals(noOfUpdations, noOfUpdateRequired);
		verify(this.upRepo).cancelScheduledPost("kishore_k", 0);
	}

	@Test
	void testFilteredUserPosts() {
		Map<String, String> insightParameters = new HashMap<String, String>();
		insightParameters.put("insightType", "month");
		insightParameters.put("month", "May");
		insightParameters.put("year", "2023");
		List<UserPosts> userPosts = new ArrayList<>();
		List<UserPostsDTO> filteredUserPostsByRepo = new ArrayList<>();
		if(insightParameters.get("insightType").equalsIgnoreCase("month")) 
			when(this.upRepo.findMonthlyInsightsByPublishedOnDate(any(String.class), any(Integer.class))).thenReturn(userPosts);
		else if(insightParameters.get("insightType").equalsIgnoreCase("quarter")) 
			when(this.upRepo.findQuarterlyInsightsByPublishedOnDate(any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(userPosts);
		else if(insightParameters.get("insightType").equalsIgnoreCase("semiannual"))
			when(this.upRepo.findHalfYearlyInsightsByPublishedOnDate(any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(userPosts);
		else if(insightParameters.get("insightType").equalsIgnoreCase("yearly"))
			when(this.upRepo.findYearlyInsightsByPublishedOnDate(any(Integer.class))).thenReturn(userPosts);
		for(UserPosts userPost : userPosts) {
			filteredUserPostsByRepo.add(modelMapper.map(userPost, UserPostsDTO.class));
		}
		List<UserPostsDTO> filteredUserPostsByService =  cmService.filteredUserPosts(insightParameters);
		verify(this.upRepo).findMonthlyInsightsByPublishedOnDate("May", 2023);
		assertEquals(filteredUserPostsByRepo, filteredUserPostsByService);
	}

	@Test
	void testGetUserPostsByUserName() {
		UserPosts userposts = new UserPosts();
		userposts.setUsername("sundhar_sg");
		List<UserPostsDTO> fetchedUserPostsByRepo = new ArrayList<>();
		when(this.upRepo.findAllUserPostsByUserName(any(String.class))).thenReturn(List.of(userposts));
		List<UserPostsDTO> fetchedUserPostsByService = this.cmService.getUserPostsByUserName("sundhar_sg");
		for(UserPosts userpost : List.of(userposts)) {
			fetchedUserPostsByRepo.add(modelMapper.map(userpost, UserPostsDTO.class));
		}
		verify(this.upRepo).findAllUserPostsByUserName(userposts.getUsername());
		assertEquals(fetchedUserPostsByRepo, fetchedUserPostsByService);
	}

//	@Test
//	void testFileValidation() {
//		assertTrue(true);
//	}
}
