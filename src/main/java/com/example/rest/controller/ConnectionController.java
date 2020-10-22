package com.example.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rest.model.Connection;
import com.example.rest.repository.ConnectionRepository;
import com.example.rest.repository.SessionRepository;
import com.example.rest.utils.AuthUtils;
import com.example.rest.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class ConnectionController {

	@Autowired
	ConnectionRepository connRepo;
	
	@Autowired
	SessionRepository sessionRepo;
	
	private AuthUtils authUtils;
	
	@Autowired
	ObjectMapper JSON;
	
	/*
	 * api point to create follow/connection between two users
	 */
	@PostMapping("/follows")
	public ResponseEntity<String> createConnection(@RequestHeader("user_id") long userId, @RequestHeader("token") String token, @RequestBody Connection connection) {
		try {
			authUtils = new AuthUtils(sessionRepo);
			if(!authUtils.validateSession(token, userId)) {
				//authentication failed
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			Connection newConn = connRepo.save(new Connection(connection.getUserId(), connection.getFollowId()));
			return new ResponseEntity<>(Utils.generateJsonResponse(JSON.writeValueAsString(newConn)), HttpStatus.CREATED);
		} catch(Exception e) {
			if(e.getCause().toString().contains("ConstraintViolationException")) {
				//in this case if one user is already following another one then bad request
				return new ResponseEntity<>(Utils.generateJsonResponse("\"already following\""), HttpStatus.BAD_REQUEST); 
			}
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
