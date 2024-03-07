package com.anton.mfa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by nadeeshan_fdz
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String getMessage(){
        return "MFA application is running...";
    }
}
