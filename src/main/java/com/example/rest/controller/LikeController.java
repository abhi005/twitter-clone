package com.example.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rest.model.Like;
import com.example.rest.repository.LikeRepository;
import com.example.rest.repository.PostRepository;
import com.example.rest.repository.SessionRepository;
import com.example.rest.utils.AuthUtils;

@RestController
@RequestMapping("/api")
public class LikeController {
	
	@Autowired
	LikeRepository likeRepo;
	
	@Autowired
	PostRepository postRepo;
	
	@Autowired
	SessionRepository sessionRepo;
	
	private AuthUtils authUtils;
	
	/*
	 * api point to like a post/tweet
	 */
	@PostMapping("/likes")
	public ResponseEntity<String> createLike(@RequestHeader("user_id") long userId, @RequestHeader("token") String token, @RequestBody Like like) {
		try {
			authUtils = new AuthUtils(sessionRepo);
			if(!authUtils.validateSession(token, userId)) {
				//authentication failed
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			//creating new entry in likes table to track which user liked which post
			likeRepo.save(new Like(like.getPostId(), userId));
			
			//increasing like count in posts table for co-responding post
			int affectedPostCount = postRepo.increaseLikeById(like.getPostId());
			if(affectedPostCount < 1) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch(Exception e) {
			if(e.getCause().toString().contains("ConstraintViolationException")) {
				// post was already liked by user
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); 
			}
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
