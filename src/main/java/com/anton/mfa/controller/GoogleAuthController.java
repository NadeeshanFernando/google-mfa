package com.anton.mfa.controller;

import com.anton.mfa.service.GoogleAuthService;
import com.anton.mfa.service.impl.GoogleAuthServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

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
     * @param secretKey
     * @param code
     * @return
     */
    @PostMapping("/validate-code")
    public ResponseEntity<?> validateCode(@RequestParam String secretKey, @RequestParam int code) {
        if(authService.validateCode(secretKey, code)){
            return ResponseEntity.status(200).body(generateToken("nadeeshanfe"));
        }
        return ResponseEntity.status(401).body("Invalid code!");
    }

    public Object generateToken(String username){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        JSONObject claims = new JSONObject();
        claims.put("username", username);

        String token = Jwts.builder()
                .setClaims(claims.toMap())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .signWith(key)
                .compact();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonResponse = objectMapper.createObjectNode();
        jsonResponse.put("access_token", token);

        return jsonResponse;
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
