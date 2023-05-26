package com.cognizant.influentia.contentms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cognizant.influentia.contentms.entity.SubscriptionPlanLimits;

public interface SubscriptionPlanLimitsRepository extends JpaRepository<SubscriptionPlanLimits, Integer> {
	
}