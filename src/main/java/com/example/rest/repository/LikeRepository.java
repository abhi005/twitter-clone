package com.example.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rest.model.Like;

public interface LikeRepository extends JpaRepository<Like, Long>{

}
