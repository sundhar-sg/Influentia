package com.cognizant.influentia.accountms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.influentia.accountms.entity.UserSocialAccounts;

public interface UserSocialAccountsRepo extends JpaRepository<UserSocialAccounts, Integer> {

	@Query(value = "SELECT * FROM usersocialaccounts WHERE userName = :userName", nativeQuery = true)
	List<UserSocialAccounts> findListOfAccountsForUser(@Param("userName") String username);
	
	@Query(value = "SELECT * FROM usersocialaccounts WHERE socialAccountType_Id = :accountTypeID", nativeQuery = true)
	List<UserSocialAccounts> findListOfAccountsBasedOnAccountTypes(@Param("accountTypeID") int accountTypeID);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "DELETE FROM usersocialaccounts WHERE socialAccountType_Id = :socialAccountTypeID and userName = :username", nativeQuery = true)
	int deleteAccount(@Param("socialAccountTypeID") int socialAccountTypeID, @Param("username") String userName);
	
	@Query(value = "SELECT * FROM usersocialaccounts WHERE socialAccountType_Id = :socialAccountTypeID and userName = :username", nativeQuery = true)
	UserSocialAccounts findUserSubscriptionByUserNameAndAccountTypeID(@Param("socialAccountTypeID") int accountTypeID, @Param("username") String userName);
}