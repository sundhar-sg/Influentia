package com.cognizant.influentia.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cognizant.influentia.security.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(value = "SELECT * FROM user WHERE username = :username", nativeQuery = true)
	public User findByUsername(@Param("username") String username);
}