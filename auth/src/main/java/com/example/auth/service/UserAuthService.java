package com.example.auth.service;

import com.example.auth.model.*;
import com.example.auth.repository.*;
import com.example.auth.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;


@Service
public class UserAuthService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Transactional
    public String registerUser(UserProfile userProfile) {
        // Check if the user already exists
        if (userAuthRepository.findByUsername(userProfile.getUsername()).isPresent()) {
            return "Username already exists!";
        }

        if (!Validation.isValidEmail(userProfile.getEmail())) {
            return "Email is not valid";
        }

        if (!Validation.isValidPhoneNumber(userProfile.getPhone())) {
            return "Phone Number is not valid";
        }

        if (userProfileRepository.findByPhone(userProfile.getPhone()).isPresent()) {
            return "Phone Number already exists!";
        }

        if (userProfileRepository.findByEmail(userProfile.getEmail()).isPresent()) {
            return "Email already exists!";
        }

        // Generate salt
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        // Hash the password with the salt
        String hashedPassword = passwordEncoder.encode(userProfile.getPassword() + encodedSalt);

        // Save the user
        UserAuth userAuth = new UserAuth();
        userAuth.setUsername(userProfile.getUsername());
        userAuth.setHashedPassword(hashedPassword);
        userAuth.setSalt(encodedSalt);
        userAuthRepository.save(userAuth);

        userProfile.setUsername(userProfile.getUsername());
        userProfileRepository.save(userProfile);
        return "User registered successfully!";
    }

    public boolean validateUser(String username, String rawPassword) {
        UserAuth userAuth = userAuthRepository.findByUsername(username).orElse(null);
        if (userAuth == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword + userAuth.getSalt(), userAuth.getHashedPassword());
    }
}
