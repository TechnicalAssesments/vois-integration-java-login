package com.example.auth.controller;

import com.example.auth.model.UserProfile;
import com.example.auth.requests.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Container
	private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.33")
			.withDatabaseName("testdb")
			.withUsername("testuser")
			.withPassword("testpass");

	@BeforeEach
	void setUp() {
		// Configure the application to use the Testcontainers MySQL instance
		System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
		System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
		System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
	}

	@Test
	void testRegister_Success() throws Exception {
		String newUser = String.valueOf(System.currentTimeMillis());
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername(newUser);
		userProfile.setPassword("password");
		userProfile.setEmail(newUser + "@example.com");
		userProfile.setPhone("+2012" + newUser.substring(newUser.length() - 7));
		userProfile.setFirstName("Test");
		userProfile.setLastName("User");

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userProfile)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.message").value("Registration successful!"));
	}

	@Test
	void testRegister_UsernameExists() throws Exception {
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername("newuser"); // Assuming this username already exists
		userProfile.setPassword("password");
		userProfile.setEmail("existinguser@example.com");
		userProfile.setPhone("+201234567891");
		userProfile.setFirstName("Test");
		userProfile.setLastName("User");

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userProfile)))
				.andExpect(status().isBadRequest()) // Adjust status based on your API response
				.andExpect(jsonPath("$.message").value("Username already exists!"));
	}

	@Test
	void testRegister_InvalidPhoneNumber() throws Exception {
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername("invalidphone");
		userProfile.setPassword("password");
		userProfile.setEmail("invalidphone@example.com");
		userProfile.setPhone("12345"); // Invalid phone number format
		userProfile.setFirstName("Test");
		userProfile.setLastName("User");

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userProfile)))
				.andExpect(status().isBadRequest()) // Adjust status based on validation rules
				.andExpect(jsonPath("$.message").value("Phone Number is not valid"));
	}

	@Test
	void testRegister_PhoneNumberExists() throws Exception {
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername("phoneexists");
		userProfile.setPassword("password");
		userProfile.setEmail("phoneexists@example.com");
		userProfile.setPhone("+201234567890");
		userProfile.setFirstName("Test");
		userProfile.setLastName("User");

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userProfile)))
				.andExpect(status().isBadRequest()) // Adjust status based on your API response
				.andExpect(jsonPath("$.message").value("Phone Number already exists!"));
	}

	@Test
	void testRegister_InvalidEmail() throws Exception {
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername("invalidemail");
		userProfile.setPassword("password");
		userProfile.setEmail("invalidemail"); // Invalid email format
		userProfile.setPhone("+201234567892");
		userProfile.setFirstName("Test");
		userProfile.setLastName("User");

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userProfile)))
				.andExpect(status().isBadRequest()) // Adjust status based on validation rules
				.andExpect(jsonPath("$.message").value("Email is not valid"));
	}

	@Test
	void testRegister_EmailExists() throws Exception {
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername("emailexists");
		userProfile.setPassword("password");
		userProfile.setEmail("newtest@example.com"); // Assuming this email already exists
		userProfile.setPhone("+201111111111");
		userProfile.setFirstName("Test");
		userProfile.setLastName("User");

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userProfile)))
				.andExpect(status().isBadRequest()) // Adjust status based on your API response
				.andExpect(jsonPath("$.message").value("Email already exists!"));
	}



	@Test
	void testLogin_Success() throws Exception {
		// First, register a user
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername("testuser");
		userProfile.setPassword("password");
		userProfile.setEmail("test@example.com");
		userProfile.setPhone("+201234567891");
		userProfile.setFirstName("Test");
		userProfile.setLastName("User");

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userProfile)));

		// Then, log in with the same credentials
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("testuser");
		loginRequest.setPassword("password");

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Login successful!"));
	}

	@Test
	void testLogin_Failure() throws Exception {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("invaliduser");
		loginRequest.setPassword("wrongpassword");

		mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Invalid username or password."));
	}
}