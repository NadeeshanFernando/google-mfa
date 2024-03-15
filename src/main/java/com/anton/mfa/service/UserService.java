package com.anton.mfa.service;

import com.anton.mfa.dto.ResponseDto;
import com.anton.mfa.model.User;

/**
 * @author by nadeeshan_fdz
 */
public interface UserService {
    ResponseDto<?> createUser(User user);

    ResponseDto<?> mfaOptionChange(String username, boolean status);
}
