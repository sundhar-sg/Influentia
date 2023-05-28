package com.cognizant.influentia.subscriptionms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.influentia.subscriptionms.entity.SubscriptionCancellations;

public interface SubscriptionCancellationsRepo extends JpaRepository<SubscriptionCancellations, Integer> {

}