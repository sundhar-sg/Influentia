package com.cognizant.influentia.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cognizant.influentia.security.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	@Query(value = "SELECT * FROM role WHERE name = :name", nativeQuery = true)
	public Role findRoleByName(@Param("name") String name);
}