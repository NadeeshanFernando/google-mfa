package com.anton.mfa.service.impl;

import com.anton.mfa.dto.ResponseDto;
import com.anton.mfa.model.User;
import com.anton.mfa.repository.UserRepository;
import com.anton.mfa.service.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    public ResponseDto<?> loginUser(User user) {
        ResponseDto<?> responseDto = new ResponseDto<>();
        User dbUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());

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
