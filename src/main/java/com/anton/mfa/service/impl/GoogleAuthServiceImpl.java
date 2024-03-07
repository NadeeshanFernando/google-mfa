package com.anton.mfa.service.impl;

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

    public GoogleAuthServiceImpl() {
        this.googleAuthenticator = new GoogleAuthenticator();
    }

    /**
     *
     * @return
     */
    @Override
    public Object generateSecretKey() {
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();
        jsonResponse.put("key", key.getKey());
        return jsonResponse;
    }

    /**
     *
     * @param secretKey
     * @param code
     * @return
     */
    @Override
    public boolean validateCode(String secretKey, int code) {
        if(googleAuthenticator.authorize(secretKey, code)){
            return true;
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
     * @param secretKey
     * @param response
     * @return
     * @throws IOException
     */
    @Override
//    public ResponseEntity<?> generateQRCode(String secretKey, HttpServletResponse response) throws IOException {
//        String otpAuthURL = generateOTPAuthURL("Square", "nadeeshanfe@allianz.lk", secretKey);
////        String otpAuthURL = "otpauth://totp/MyAppName:user@example.com?secret=" + secretKey + "&issuer=MyAppName";
//        int width = 300;
//        int height = 300;
//        Map<EncodeHintType, Object> hints = new HashMap<>();
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        try {
//            BitMatrix bitMatrix = new QRCodeWriter().encode(otpAuthURL, BarcodeFormat.QR_CODE, width, height, hints);
//            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", response.getOutputStream());
//        } catch (WriterException e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate QR code");
//        }
//        return null;
//    }
    public ResponseEntity<?> generateQRCode(String secretKey, HttpServletResponse response) {
        String otpAuthURL = generateOTPAuthURL("Square", "nadeeshanfe@allianz.lk", secretKey);
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

            return ResponseEntity.ok().body(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate QR code");
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
