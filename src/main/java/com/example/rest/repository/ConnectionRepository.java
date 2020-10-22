package com.example.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rest.model.Connection;

public interface ConnectionRepository extends JpaRepository<Connection, Long>{

}
