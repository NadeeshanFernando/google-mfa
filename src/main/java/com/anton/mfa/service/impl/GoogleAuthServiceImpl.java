package com.anton.mfa.service.impl;

import com.anton.mfa.dto.ResponseDto;
import com.anton.mfa.model.Users;
import com.anton.mfa.repository.UserRepository;
import com.anton.mfa.service.Authentication;
import com.anton.mfa.service.GoogleAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by nadeeshan_fdz
 */
@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final GoogleAuthenticator googleAuthenticator;

    @Autowired
    private UserRepository userRepository;

    public GoogleAuthServiceImpl() {
        this.googleAuthenticator = new GoogleAuthenticator();
    }

    /**
     *
     * @return
     */
    @Override
    public String generateSecretKey() {
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        return key.getKey();
    }

    /**
     *
     * @param username
     * @param code
     * @return
     */
    @Override
    public boolean validateCode(String username, int code) {
        Users dbUser = userRepository.findByUsername(username);
        if(dbUser != null){
            if(googleAuthenticator.authorize(dbUser.getSecretKey(), code)){
                if(!dbUser.isMfaEnabled()){
                    dbUser.setMfaEnabled(true);
                    userRepository.save(dbUser);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     *
     * @param secretKey
     * @return
     */
    @Override
    public boolean isValidSecretKeyFormat(String secretKey) {
        if (secretKey == null || secretKey.isEmpty()) {
            return false;
        }
        return secretKey != null && secretKey.length() >= 16;
    }

    /**
     *
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    public ResponseDto<?> generateQRCode(String username, HttpServletResponse response) {
        ResponseDto<?> responseDto = new ResponseDto<>();
        Users dbUser = userRepository.findByUsername(username);
        if(dbUser != null){
            if(!dbUser.isMfaEnabled()) {
                String secretKey = generateSecretKey();
                dbUser.setSecretKey(secretKey);
                userRepository.save(dbUser);

                String otpAuthURL = generateOTPAuthURL("Square", username + "@allianz.lk", dbUser.getSecretKey());
                int width = 300;
                int height = 300;
                Map<EncodeHintType, Object> hints = new HashMap<>();
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    BitMatrix bitMatrix = new QRCodeWriter().encode(otpAuthURL, BarcodeFormat.QR_CODE, width, height, hints);
                    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

                    byte[] qrImageData = outputStream.toByteArray();
                    String qrImageBase64 = Base64.getEncoder().encodeToString(qrImageData);

                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("qrImageBase64", qrImageBase64);

                    responseDto.setStatus(HttpStatus.OK.value());
                    responseDto.setMessage("QR code generated");
                    responseDto.setData(qrImageBase64);
                    return responseDto;
                } catch (Exception e) {
                    e.printStackTrace();
                    responseDto.setStatus(HttpStatus.OK.value());
                    responseDto.setMessage("QR code not generated");
                    responseDto.setData(null);
                    return responseDto;
                }
            }
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("User already registered with Google Authenticator");
            responseDto.setData(null);
            return responseDto;
        }
        else{
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setMessage("Invalid username or password");
            responseDto.setData(null);
            return responseDto;
        }
    }

    /**
     *
     * @param appName
     * @param userEmail
     * @param secretKey
     * @return
     */
    @Override
    public String generateOTPAuthURL(String appName, String userEmail, String secretKey) {
        String otpAuthURL = null;
        try {
            otpAuthURL = "otpauth://totp/" + appName + ":" + userEmail + "?secret=" + secretKey + "&issuer=" + appName;
//            otpAuthURL = new URI(null, null, otpAuthURL, null).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return otpAuthURL;
    }

}
