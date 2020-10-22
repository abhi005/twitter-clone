DROP TABLE IF EXISTS users;

/*creating users table*/
CREATE TABLE users (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	user_name VARCHAR(250) NOT NULL UNIQUE,
	first_name VARCHAR(250) NOT NULL,
	last_name VARCHAR(250),
	password VARCHAR(250) NOT NULL,
	active BOOLEAN DEFAULT TRUE,
	created_at TIMESTAMP DEFAULT now(),
	updated_at TIMESTAMP DEFAULT now()
);

/*feeding user table data*/
INSERT INTO users (user_name, first_name, last_name, password) VALUES ('abhi.hi', 'abhishek', 'jain', 'happiness');
INSERT INTO users (user_name, first_name, last_name, password) VALUES ('abhihi', 'abhishek', 'jain', 'happiness');


DROP TABLE IF EXISTS sessions;

/*creating session table*/
CREATE TABLE sessions (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	user_id BIGINT NOT NULL,
	token TEXT NOT NULL,
	active BOOLEAN DEFAULT TRUE,
	created_at TIMESTAMP DEFAULT now(),
	expires_at TIMESTAMP DEFAULT now()
);

/*feeding session table data*/
INSERT INTO sessions (user_id, token) VALUES(1, 'abcd');
INSERT INTO sessions (user_id, token) VALUES(2, 'efgh');


DROP TABLE IF EXISTS posts;

/*creating posts table*/
CREATE TABLE posts (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	user_id BIGINT NOT NULL,
	data TEXT NOT NULL,
	like_count INT DEFAULT 0,
	created_at TIMESTAMP DEFAULT now(),
	updated_at TIMESTAMP DEFAULT now()
);



DROP TABLE IF EXISTS connections;

/*creating connections table*/
CREATE TABLE connections (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	user_id BIGINT NOT NULL,
	follow_id BIGINT NOT NULL,
	CONSTRAINT UC_Connection UNIQUE(user_id, follow_id)
);


DROP TABLE IF EXISTS likes;

/*creating likes table*/
CREATE TABLE likes (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	post_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	CONSTRAINT UC_Like UNIQUE(post_id, user_id)
);