package com.anton.mfa.service;

import com.anton.mfa.model.Users;
import org.springframework.http.ResponseEntity;

/**
 * @author by nadeeshan_fdz
 */
public interface Authentication {
    ResponseEntity<?> login(Users user);
}
