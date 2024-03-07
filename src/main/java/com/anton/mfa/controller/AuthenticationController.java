package com.anton.mfa.controller;

import com.anton.mfa.model.Users;
import com.anton.mfa.repository.UserRepository;
import com.anton.mfa.service.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by nadeeshan_fdz
 */
@RestController
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private Authentication authentication;

    /**
     *
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        return authentication.login(user);
    }
}
