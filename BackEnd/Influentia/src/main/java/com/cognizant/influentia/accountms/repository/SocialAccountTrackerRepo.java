package com.cognizant.influentia.accountms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.influentia.accountms.entity.SocialAccountTracker;

public interface SocialAccountTrackerRepo extends JpaRepository<SocialAccountTracker, Integer> {

}