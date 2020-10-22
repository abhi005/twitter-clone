package com.example.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rest.model.Post;
import com.example.rest.model.User;
import com.example.rest.repository.PostRepository;
import com.example.rest.repository.SessionRepository;
import com.example.rest.repository.UserRepository;
import com.example.rest.utils.AuthUtils;
import com.example.rest.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	PostRepository postRepo;
	
	private AuthUtils authUtils;
	
	@Autowired
	ObjectMapper JSON;

	/*
	 * api point for creating new user / registering new user
	 */
	@PostMapping("/users")
	public ResponseEntity<String> createUser(@RequestBody User user) {
		try {
			User newUser = userRepository.save(new User(user.getUserName(), user.getFirstName().toLowerCase(), user.getLastName().toLowerCase(), user.getPassword(), true));
			return new ResponseEntity<>(Utils.generateJsonResponse(JSON.writeValueAsString(newUser)), HttpStatus.CREATED);
		} catch(Exception e) {
			// in this case username is not unique and already exist in system.
			if(e.getCause().toString().contains("ConstraintViolationException")) {
				return new ResponseEntity<>(Utils.generateJsonResponse("\"username not available try again\""), HttpStatus.BAD_REQUEST); 
			}
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * api point for searching user by their user handles, first name, last name.
	 */
	@GetMapping("/users/{query}")
	public ResponseEntity<String> getUserByUserName(@RequestHeader("user_id") long userId, @RequestHeader("token") String token, @PathVariable("query") String userName) {
		try {
			authUtils = new AuthUtils(sessionRepo);
			if(!authUtils.validateSession(token, userId)) {
				//authentication failed
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			List<User> userData = userRepository.findUserByKey(userName.toLowerCase());
			if(userData.size() < 1) {
				//no user available for given query
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			
			return new ResponseEntity<>(Utils.generateJsonResponse(JSON.writeValueAsString(userData)), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * sends personalized feed for user based on user's following list
	 */
	@GetMapping("/feed")
	public ResponseEntity<String> getFeedByUser(@RequestHeader("user_id") long userId, @RequestHeader("token") String token) {
		try {
			authUtils = new AuthUtils(sessionRepo);
			if(!authUtils.validateSession(token, userId)) {
				//authentication failed
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			List<Post> feedData = postRepo.getUserFeed(userId);
			
			return new ResponseEntity<>(Utils.generateJsonResponse(JSON.writeValueAsString(feedData)), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
