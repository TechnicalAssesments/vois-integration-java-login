package com.example.auth.controller;

import com.example.auth.model.UserProfile;
import com.example.auth.requests.LoginRequest;
import com.example.auth.responses.StringResponse;
import com.example.auth.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserAuthService userAuthService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<StringResponse> register(@RequestBody UserProfile userProfile) {
        try {
            String response = userAuthService.registerUser(userProfile);

            if (!response.equals("User registered successfully!")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new StringResponse(response));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new StringResponse("Registration successful!"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StringResponse("An error occurred during registration."));
        }
    }

    // Login a user
    @PostMapping("/login")
    public ResponseEntity<StringResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            boolean isValid = userAuthService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());

            if (isValid) {
                return ResponseEntity.ok(new StringResponse("Login successful!"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new StringResponse("Invalid username or password."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StringResponse("An error occurred during login."));
        }
    }
}
