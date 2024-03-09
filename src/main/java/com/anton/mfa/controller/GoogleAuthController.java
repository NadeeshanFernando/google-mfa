package com.anton.mfa.controller;

import com.anton.mfa.dto.GenerateQRRequest;
import com.anton.mfa.dto.ValidateCodeRequest;
import com.anton.mfa.dto.ResponseDto;
import com.anton.mfa.service.GoogleAuthService;
import com.anton.mfa.service.impl.GoogleAuthServiceImpl;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author by nadeeshan_fdz
 */
@RestController
@CrossOrigin(origins = "*")
public class GoogleAuthController {

    private final GoogleAuthService authService;
    private final GoogleAuthenticator googleAuthenticator;

    @Autowired
    public GoogleAuthController(GoogleAuthServiceImpl authService) {
        this.authService = authService;
        this.googleAuthenticator = new GoogleAuthenticator(new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder().build());
    }

    /**
     *
     * @return
     */
    @GetMapping("/generate-secret")
    public ResponseEntity<?> generateSecretKey() {
        Object secretKey = authService.generateSecretKey();
        return ResponseEntity.ok(secretKey);
    }

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/validate-code")
    public ResponseEntity<?> validateCode(@RequestBody ValidateCodeRequest request) {
        ResponseDto<?> responseDto = authService.validateCode(request.getUsername(), request.getCode());
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    /**
     *
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/generate-qr")
    public ResponseEntity<?> generateQRCode(@RequestBody GenerateQRRequest request) throws IOException {
        ResponseDto<?> responseDto = authService.generateQRCode(request.getUsername());
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

}
