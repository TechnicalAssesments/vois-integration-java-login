create schema userDB;

use userDB;

CREATE TABLE user_auth (
    username VARCHAR(255) PRIMARY KEY,
    hashed_password VARCHAR(255) NOT NULL, 
    salt VARCHAR(255) NOT NULL 
);

CREATE TABLE user_info (
    username VARCHAR(255) PRIMARY KEY, 
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL, 
    last_name VARCHAR(255) NOT NULL 
);