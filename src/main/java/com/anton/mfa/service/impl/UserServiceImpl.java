package com.anton.mfa.service.impl;

import com.anton.mfa.dto.ResponseDto;
import com.anton.mfa.model.User;
import com.anton.mfa.repository.UserRepository;
import com.anton.mfa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author by nadeeshan_fdz
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseDto<?> createUser(User user) {
        ResponseDto<?> responseDto = new ResponseDto<>();
        User dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser != null) {
            responseDto.setStatus(HttpStatus.CONFLICT.value());
            responseDto.setMessage("Username already exist");
            responseDto.setData(null);
            return responseDto;
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setSecretKey(user.getSecretKey());
        newUser.setMfaEnabled(false);

        userRepository.save(newUser);
        responseDto.setStatus(HttpStatus.CREATED.value());
        responseDto.setMessage("User created successfully");
        responseDto.setData(newUser);
        return responseDto;
    }

    @Override
    public ResponseDto<?> mfaOptionChange(String username, boolean status) {
        ResponseDto<?> responseDto = new ResponseDto<>();
        User dbUser = userRepository.findByUsername(username);
        if (dbUser != null) {
            dbUser.setMfaEnabled(status);
            userRepository.save(dbUser);
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("MFA Option changed to "+status);
            responseDto.setData(null);
            return responseDto;
        }
        responseDto.setStatus(HttpStatus.NOT_FOUND.value());
        responseDto.setMessage("Username not found");
        responseDto.setData(null);
        return responseDto;
    }
}
