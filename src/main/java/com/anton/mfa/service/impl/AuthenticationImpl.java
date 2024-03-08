package com.anton.mfa.service.impl;

import com.anton.mfa.dto.ResponseDto;
import com.anton.mfa.model.Users;
import com.anton.mfa.repository.UserRepository;
import com.anton.mfa.service.Authentication;
import com.anton.mfa.service.GoogleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     *
     * @param user
     * @return
     */
    @Override
    public ResponseDto<?> loginUser(Users user) {
        ResponseDto<?> responseDto = new ResponseDto<>();
        Users dbUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());

        if (dbUser != null) {
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Login Success");
            responseDto.setData(null);
            return responseDto;
        } else {
            responseDto.setStatus(HttpStatus.UNAUTHORIZED.value());
            responseDto.setMessage("Invalid username or password");
            responseDto.setData(null);
            return responseDto;
        }
    }
}
