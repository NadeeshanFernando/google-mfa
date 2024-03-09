package com.anton.mfa.service;

import com.anton.mfa.dto.ResponseDto;

import java.io.IOException;

/**
 * @author by nadeeshan_fdz
 */
public interface GoogleAuthService {

    public String generateSecretKey();

    public ResponseDto<?> validateCode(String username, int code);

    public boolean isValidSecretKeyFormat(String secretKey);

    public ResponseDto<?> generateQRCode(String username) throws IOException;

    public String generateOTPAuthURL(String appName, String userEmail, String secretKey);
}
