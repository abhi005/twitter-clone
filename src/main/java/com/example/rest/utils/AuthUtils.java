package com.example.rest.utils;

import java.util.List;
import java.util.UUID;

import com.example.rest.model.Session;
import com.example.rest.model.User;
import com.example.rest.repository.SessionRepository;

public class AuthUtils {
	
	private SessionRepository sessionRepo;
	
	public AuthUtils(SessionRepository repo) {
		this.sessionRepo = repo;
	}
	
	/*
	 * method generates new session token and creates entry in sessions table
	 */
	public Session createNewSession(User user) {
		String token = UUID.randomUUID().toString();
		Session session = new Session(user.getId(), token);
		Session newSession = sessionRepo.save(session);
		return newSession;
	}
	
	/*
	 * method expires all active sessions for given userid
	 */
	public int expireAllSessions(long userId) {
		return sessionRepo.updateActiveByUserId(userId, false);
	}
	
	/*
	 * method validates active session for given userid and session token from sessions table
	 */
	public boolean validateSession(String token, long userId) {
		List<Session> sessions = sessionRepo.getActiveSessionByTokenAndUserId(token, userId);
		return sessions.size() > 0 ? true : false;
	}
}
