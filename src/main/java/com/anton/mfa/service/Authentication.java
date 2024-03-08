package com.anton.mfa.service;

import com.anton.mfa.dto.ResponseDto;
import com.anton.mfa.model.Users;
import org.springframework.http.ResponseEntity;

/**
 * @author by nadeeshan_fdz
 */
public interface Authentication {
    public ResponseDto<?> loginUser(Users user);
}
