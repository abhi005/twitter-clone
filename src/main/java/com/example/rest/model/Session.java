package com.example.rest.model;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sessions")
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "user_id")
	private long userId;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "active")
	private boolean active;
	
	@Column(name = "created_at")
	private String createdAt;
	
	@Column(name = "expires_at")
	private String expiresAt;
	
	public Session() {}
	
	public Session(long userId, String token) {
		this.userId = userId;
		this.token = token;
		this.active = true;
		long millis = System.currentTimeMillis();
		this.createdAt = new Timestamp(millis).toString();
		this.expiresAt = new Timestamp(millis + TimeUnit.MINUTES.toMillis(30)).toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}

	@Override
	public String toString() {
		return "Session [id=" + id + ", userId=" + userId + ", token=" + token + ", active=" + active + ", createdAt="
				+ createdAt + ", expiresAt=" + expiresAt + "]";
	}
	
	
}
