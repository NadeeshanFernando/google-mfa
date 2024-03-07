package com.anton.mfa.service.impl;

import com.anton.mfa.service.GoogleAuthService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
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
    public String generateSecretKey() {
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        return key.getKey();
    }

    /**
     *
     * @param secretKey
     * @param code
     * @return
     */
    @Override
    public boolean validateCode(String secretKey, int code) {
        return googleAuthenticator.authorize(secretKey, code);
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
    public ResponseEntity<?> generateQRCode(String secretKey, HttpServletResponse response) throws IOException {
        String otpAuthURL = generateOTPAuthURL("Square", "nadeeshanfe@allianz.lk", secretKey);
//        String otpAuthURL = "otpauth://totp/MyAppName:user@example.com?secret=" + secretKey + "&issuer=MyAppName";
        int width = 300;
        int height = 300;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(otpAuthURL, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", response.getOutputStream());
        } catch (WriterException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate QR code");
        }
        return null;
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
