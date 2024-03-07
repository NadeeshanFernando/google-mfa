package com.anton.mfa.service.impl;

import com.anton.mfa.model.Users;
import com.anton.mfa.repository.UserRepository;
import com.anton.mfa.service.Authentication;
import com.anton.mfa.service.GoogleAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * @author by nadeeshan_fdz
 */
@Service
public class AuthenticationImpl implements Authentication {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoogleAuthService googleAuthService;

    /**
     *
     * @param user
     * @return
     */
    @Override
    public ResponseEntity<?> login(Users user) {
        Users dbUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());

        if (dbUser != null) {
            Object jsonResponse = googleAuthService.generateSecretKey();
            return ResponseEntity.ok().body(jsonResponse);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
