package com.anton.mfa.service;

import com.anton.mfa.dto.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * @author by nadeeshan_fdz
 */
public interface GoogleAuthService {

    public String generateSecretKey();

    public boolean validateCode(String secretKey, int code);

    public boolean isValidSecretKeyFormat(String secretKey);

    public ResponseDto<?> generateQRCode(String username, HttpServletResponse response) throws IOException;

    public String generateOTPAuthURL(String appName, String userEmail, String secretKey);
}
