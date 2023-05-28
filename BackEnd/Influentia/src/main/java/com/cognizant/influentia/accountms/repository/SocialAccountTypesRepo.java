package com.cognizant.influentia.accountms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cognizant.influentia.accountms.entity.SocialAccountTypes;

public interface SocialAccountTypesRepo extends JpaRepository<SocialAccountTypes, Integer> {

	@Query(value = "SELECT * FROM SocialAccountTypes", nativeQuery = true)
	List<SocialAccountTypes> findAllAccountTypes();
}