package com.cognizant.influentia.contentms.repository;

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
	@Query("UPDATE UserPosts as up SET up.postStatus = 'Cancelled' WHERE up.username = :username and up.id = :ID")
	int cancelScheduledPost(@Param("username") String username, @Param("ID") int id);
	
	@Query(value = "SELECT * FROM UserPosts WHERE id = :ID", nativeQuery = true)
	UserPosts findObjById(@Param("ID") Integer id);
	
	@Query(value = "SELECT * FROM UserPosts WHERE MONTHNAME(publishedOnDate) = :monthName AND YEAR(publishedOnDate) = :year", nativeQuery = true)
	List<UserPosts> findMonthlyInsightsByPublishedOnDate(@Param("monthName") String monthName, @Param("year") int year);
	
	@Query(value = "SELECT * FROM UserPosts WHERE MONTH(publishedOnDate) >= :startMonth AND MONTH(publishedOnDate) <= :endMonth AND YEAR(publishedOnDate) = :year", nativeQuery = true)
	List<UserPosts> findQuarterlyInsightsByPublishedOnDate(@Param("startMonth") int startingMonth, @Param("endMonth") int endingMonth, @Param("year") int year);
	
	@Query(value = "SELECT * FROM UserPosts WHERE MONTH(publishedOnDate) >= :startMonth AND MONTH(publishedOnDate) <= :endMonth AND YEAR(publishedOnDate) = :year", nativeQuery = true)
	List<UserPosts> findHalfYearlyInsightsByPublishedOnDate(@Param("startMonth") int startingMonth, @Param("endMonth") int endingMonth, @Param("year") int year);
	
	@Query(value = "SELECT * FROM UserPosts WHERE YEAR(publishedOnDate) = :year", nativeQuery = true)
	List<UserPosts> findYearlyInsightsByPublishedOnDate(@Param("year") int year);
	
	@Query(value = "SELECT * FROM UserPosts WHERE username = :username", nativeQuery = true)
	List<UserPosts> findAllUserPostsByUserName(@Param("username") String username);
}