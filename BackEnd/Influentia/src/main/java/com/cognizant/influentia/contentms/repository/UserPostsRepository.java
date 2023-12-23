package com.cognizant.influentia.contentms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.influentia.contentms.entity.UserPosts;

public interface UserPostsRepository extends JpaRepository<UserPosts, Integer> {
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("UPDATE userposts as up SET up.postStatus = 'Cancelled' WHERE up.username = :username and up.id = :ID")
	int cancelScheduledPost(@Param("username") String username, @Param("ID") int id);
	
	@Query(value = "SELECT * FROM userposts WHERE id = :ID", nativeQuery = true)
	UserPosts findObjById(@Param("ID") Integer id);
	
	@Query(value = "SELECT COUNT(*) FROM userposts WHERE MONTHNAME(publishedOnDate) = :monthName AND YEAR(publishedOnDate) = :year AND socialNetworkType = :socialAccountType AND username = :username AND postType = :postType", nativeQuery = true)
	int findMonthlyInsightsByPublishedOnDate(@Param("monthName") String monthName, @Param("year") int year, @Param("socialAccountType") String socialAccountType, @Param("username") String username, @Param("postType") String postType);
	
	@Query(value = "SELECT COUNT(*) FROM userposts WHERE MONTH(publishedOnDate) >= :startMonth AND MONTH(publishedOnDate) <= :endMonth AND YEAR(publishedOnDate) = :year AND socialNetworkType = :socialAccountType AND username = :username AND postType = :postType", nativeQuery = true)
	int findQuarterlyInsightsByPublishedOnDate(@Param("startMonth") int startingMonth, @Param("endMonth") int endingMonth, @Param("year") int year, @Param("socialAccountType") String socialAccountType, @Param("username") String username, @Param("postType") String postType);
	
	@Query(value = "SELECT COUNT(*) FROM userposts WHERE MONTH(publishedOnDate) >= :startMonth AND MONTH(publishedOnDate) <= :endMonth AND YEAR(publishedOnDate) = :year AND socialNetworkType = :socialAccountType AND username = :username AND postType = :postType", nativeQuery = true)
	int findHalfYearlyInsightsByPublishedOnDate(@Param("startMonth") int startingMonth, @Param("endMonth") int endingMonth, @Param("year") int year, @Param("socialAccountType") String socialAccountType, @Param("username") String username, @Param("postType") String postType);
	
	@Query(value = "SELECT COUNT(*) FROM userposts WHERE YEAR(publishedOnDate) = :year AND socialNetworkType = :socialAccountType AND username = :username AND postType = :postType", nativeQuery = true)
	int findYearlyInsightsByPublishedOnDate(@Param("year") int year, @Param("socialAccountType") String socialAccountType, @Param("username") String username, @Param("postType") String postType);
	
	@Query(value = "SELECT * FROM userposts WHERE username = :username", nativeQuery = true)
	List<UserPosts> findAllUserPostsByUserName(@Param("username") String username);
	
	@Query(value = "SELECT COUNT(*) FROM userposts WHERE username = :username AND MONTH(publishedOnDate) = :publishedMonth AND postStatus = 'Scheduled'", nativeQuery = true)
	int findNumberOfPostsBasedOnUserName(@Param("username") String username, @Param("publishedMonth") int publishedMonth);
	
	@Query(value = "SELECT DISTINCT(socialNetworkType) FROM userposts WHERE username = :username", nativeQuery = true)
	List<String> numberOfUniqueAccountsForUser(@Param("username") String username);
	
	@Query(value = "SELECT COUNT(*) FROM userposts WHERE username = :username AND publishedOnDate >= :startDate AND publishedOnDate <= :endDate AND socialNetworktype = :socialAccountType AND postType = :postType", nativeQuery = true)
	int findCustomInsightsByPublishedOnDate(@Param("username") String username, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("socialAccountType") String socialAccountType, @Param("postType") String postType);
}