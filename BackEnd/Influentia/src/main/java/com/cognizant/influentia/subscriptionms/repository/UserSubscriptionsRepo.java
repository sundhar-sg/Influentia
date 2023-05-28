package com.cognizant.influentia.subscriptionms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cognizant.influentia.subscriptionms.entity.UserSubscriptions;

import jakarta.transaction.Transactional;

public interface UserSubscriptionsRepo extends JpaRepository<UserSubscriptions, Integer> {

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE UserSubscriptions SET subscriptionStatus = :Status WHERE id = :ID")
	int UpdateSubscription(@Param("ID") int id, @Param("Status") String status);
	
	@Query(value = "SELECT * FROM UserSubscriptions WHERE userName = :userName", nativeQuery = true)
	UserSubscriptions findSubscriptionByUsername(@Param("userName") String userName);
}