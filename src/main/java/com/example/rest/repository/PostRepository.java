package com.example.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>{

	/*
	 * method to update posts table for increasing like count of post
	 */
	@Transactional @Modifying
	@Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
	int increaseLikeById(@Param("postId") long postId);
	
	/*
	 * method returns all the posts from users who are followed by given user in order of there last modification time
	 */
	@Query("SELECT p FROM Post p WHERE p.userId IN (SELECT c.followId FROM Connection c WHERE c.userId = :userId) ORDER BY p.updatedAt DESC")
	List<Post> getUserFeed(@Param("userId") long userId);
}
