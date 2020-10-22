package com.example.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rest.model.Session;
import com.example.rest.model.User;
import com.example.rest.repository.SessionRepository;
import com.example.rest.repository.UserRepository;
import com.example.rest.utils.AuthUtils;
import com.example.rest.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class SessionController {

	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	UserRepository userRepo;
	
	private AuthUtils authUtils;
	
	@Autowired
	ObjectMapper JSON;
	
	/*
	 * api point to create new session for particular user
	 */
	@PostMapping("/login")
	public ResponseEntity<String> createSession(@RequestBody User user) {
		try {
			authUtils = new AuthUtils(sessionRepo);
			List<User> u = userRepo.getUserByUserName(user.getUserName());
			
			if(u.size() != 0 && u.get(0).getPassword().equals(user.getPassword())) {
				
				//expiring all old session before creating new one for given user
				authUtils.expireAllSessions(u.get(0).getId());
				
				//creating new session for user
				Session newSession = authUtils.createNewSession(u.get(0));
				return new ResponseEntity<>(Utils.generateJsonResponse(JSON.writeValueAsString(newSession)), HttpStatus.OK);
				
			} else {
				//authentication failed
				return new ResponseEntity<>(Utils.generateJsonResponse("\"username or password is incorrect\""), HttpStatus.UNAUTHORIZED);
			}
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * api point for destroying user session
	 */
	@PostMapping("/logout")
	public ResponseEntity<String> expireSession(@RequestHeader("user_id") long userId, @RequestHeader("token") String token, @RequestBody Session session) {
		try {
			authUtils = new AuthUtils(sessionRepo);
			if(!authUtils.validateSession(token, userId)) {
				//authentication failed
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			int expiredSessionCount = authUtils.expireAllSessions(userId);
			if(expiredSessionCount == 0) {
				//this case means there was active session for given user in first place
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
