package com.example.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rest.model.Post;
import com.example.rest.repository.PostRepository;
import com.example.rest.repository.SessionRepository;
import com.example.rest.utils.AuthUtils;
import com.example.rest.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class PostController {
	
	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	PostRepository postRepo;
	
	private AuthUtils authUtils;
	
	@Autowired
	ObjectMapper JSON;
	
	/*
	 * api point to create new post/tweet
	 */
	@PostMapping("/posts")
	public ResponseEntity<String> createPost(@RequestHeader("user_id") long userId, @RequestHeader("token") String token, @RequestBody Post post) {
		try {
			authUtils = new AuthUtils(sessionRepo);
			if(post.getData().length() > 140) {
				//if tweet length is more than 140 chars then will raise error
				return new ResponseEntity<>(Utils.generateJsonResponse("\"tweet should be only 140 characters long\""), HttpStatus.BAD_REQUEST);
			}
			if(!authUtils.validateSession(token, userId)) {
				//authentication failed
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			Post newPost = postRepo.save(new Post(post.getData(), userId));
			return new ResponseEntity<>(Utils.generateJsonResponse(JSON.writeValueAsString(newPost)), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
