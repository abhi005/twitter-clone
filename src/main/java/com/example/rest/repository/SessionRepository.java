package com.example.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.model.Session;

public interface SessionRepository extends JpaRepository<Session, Long>{

	/*
	 * method updates sessions table to change active state of session for particular user
	 */
	@Transactional @Modifying
	@Query("UPDATE Session s SET s.active = :flag WHERE s.userId = :userId")
	int updateActiveByUserId(@Param("userId") long userId, @Param("flag") boolean flag);
	
	/*
	 * method return list of active session based on userid and session token
	 */
	@Query("SELECT s FROM Session s WHERE s.token = :token AND s.userId = :userId AND s.active = true")
	List<Session> getActiveSessionByTokenAndUserId(@Param("token") String token, @Param("userId") long userId);
}
