package com.cognizant.influentia.subscriptionms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cognizant.influentia.subscriptionms.entity.UserSubscriptions;

import jakarta.transaction.Transactional;

public interface UserSubscriptionsRepo extends JpaRepository<UserSubscriptions, Integer> {

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE usersubscriptions SET subscriptionStatus = :Status WHERE id = :ID")
	int UpdateSubscription(@Param("ID") int id, @Param("Status") String status);
	
	@Query(value = "SELECT * FROM usersubscriptions WHERE userName = :userName", nativeQuery = true)
	UserSubscriptions findSubscriptionByUsername(@Param("userName") String userName);
	
	@Query(value = "SELECT * FROM usersubscriptions WHERE planID = :planID", nativeQuery = true)
	List<UserSubscriptions> findUserSubscriptionsByPlanID(@Param("planID") int planID);
}