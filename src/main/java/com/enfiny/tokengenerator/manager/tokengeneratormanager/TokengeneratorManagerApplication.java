package com.enfiny.tokengenerator.manager.tokengeneratormanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
public class TokengeneratorManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokengeneratorManagerApplication.class, args);
	}
}
