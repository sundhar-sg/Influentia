package com.cognizant.influentia.accountms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cognizant.influentia.accountms.entity.UserSocialAccounts;

public interface UserSocialAccountsRepo extends JpaRepository<UserSocialAccounts, Integer> {

	@Query(value = "SELECT * FROM UserSocialAccounts WHERE userName = :userName", nativeQuery = true)
	List<UserSocialAccounts> findListOfAccountsForUser(@Param("userName") String username);
	
	@Query(value = "DELETE FROM UserSocialAccounts WHERE id = :ID", nativeQuery = true)
	boolean deleteAccount(@Param("ID") int id);
}