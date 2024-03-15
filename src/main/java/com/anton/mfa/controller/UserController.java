package com.anton.mfa.controller;

import com.anton.mfa.dto.ResponseDto;
import com.anton.mfa.dto.ValidateCodeRequest;
import com.anton.mfa.model.User;
import com.anton.mfa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by nadeeshan_fdz
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        ResponseDto<?> responseDto = userService.createUser(user);
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @PutMapping("/mfa-option-change")
    public ResponseEntity<?> mfaOptionChange(@RequestBody String username, boolean status) {
        ResponseDto<?> responseDto = userService.mfaOptionChange(username, status);
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }
}
