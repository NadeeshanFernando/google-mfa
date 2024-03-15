package com.anton.mfa.controller;

import com.anton.mfa.dto.ResponseDto;
import com.anton.mfa.model.User;
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
    public ResponseEntity<?> login(@RequestBody User user) {
        ResponseDto<?> responseDto = authentication.loginUser(user);
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }
}
