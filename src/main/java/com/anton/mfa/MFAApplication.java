package com.anton.mfa;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

@SpringBootApplication
//@EnableScheduling
@Slf4j
public class MFAApplication {

//	private final GoogleAuthenticator googleAuthenticator;
//
//	public MFAApplication() {
//		this.googleAuthenticator = new GoogleAuthenticator();
//	}

	public static void main(String[] args) {
		SpringApplication.run(MFAApplication.class, args);
	}

//	@Scheduled(fixedRate = 1000L)
//	public String ping(){
//
//		return null;
//	}

}
