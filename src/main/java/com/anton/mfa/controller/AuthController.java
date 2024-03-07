package com.anton.mfa.controller;

import com.anton.mfa.service.GoogleAuthService;
import com.anton.mfa.service.impl.GoogleAuthServiceImpl;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author by nadeeshan_fdz
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final GoogleAuthService authService;
    private final GoogleAuthenticator googleAuthenticator;

    @Autowired
    public AuthController(GoogleAuthServiceImpl authService) {
        this.authService = authService;
        this.googleAuthenticator = new GoogleAuthenticator(new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder().build());
    }

    /**
     *
     * @return
     */
    @GetMapping("/generate-secret")
    public ResponseEntity<String> generateSecretKey() {
        String secretKey = authService.generateSecretKey();
        return ResponseEntity.ok(secretKey);
    }

    /**
     *
     * @param secretKey
     * @param code
     * @return
     */
    @PostMapping("/validate-code")
    public ResponseEntity<String> validateCode(@RequestParam String secretKey, @RequestParam int code) {
        if (secretKey == null || secretKey.isEmpty()) {
            return ResponseEntity.badRequest().body("Secret key cannot be empty");
        }
        if (!authService.isValidSecretKeyFormat(secretKey)) {
            return ResponseEntity.badRequest().body("Invalid secret key format");
        }
        if (!authService.validateCode(secretKey, code)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid code");
        }
        return ResponseEntity.ok("Code is valid");
    }

    /**
     *
     * @param secretKey
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/generate-qr/{secretKey}")
    public ResponseEntity<?> generateQRCode(@PathVariable String secretKey, HttpServletResponse response) throws IOException {
        if (secretKey == null || secretKey.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Secret key cannot be empty");
            return ResponseEntity.badRequest().body("Secret key cannot be empty");
        }
        if (!authService.isValidSecretKeyFormat(secretKey)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid secret key format");
            return ResponseEntity.badRequest().body("Invalid secret key format");
        }
        return authService.generateQRCode(secretKey, response);
    }

}
