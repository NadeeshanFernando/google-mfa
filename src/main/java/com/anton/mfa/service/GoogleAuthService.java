package com.anton.mfa.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * @author by nadeeshan_fdz
 */
public interface GoogleAuthService {

    public Object generateSecretKey();

    public boolean validateCode(String secretKey, int code);

    public boolean isValidSecretKeyFormat(String secretKey);

    public ResponseEntity<?> generateQRCode(String secretKey, HttpServletResponse response) throws IOException;

    public String generateOTPAuthURL(String appName, String userEmail, String secretKey);
}
