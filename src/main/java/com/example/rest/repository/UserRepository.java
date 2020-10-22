package com.example.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.rest.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	/*
	 * method return list of user based on search query
	 */
	@Query("SELECT u from User u WHERE u.userName LIKE %:query% OR u.firstName LIKE %:query% OR u.lastName LIKE %:query%")
	List<User> findUserByKey(@Param("query") String query);
	
	/*
	 * method return list of users with given username
	 */
	@Query("SELECT u from User u WHERE u.userName = :userName")
	List<User> getUserByUserName(@Param("userName") String userName);
}
