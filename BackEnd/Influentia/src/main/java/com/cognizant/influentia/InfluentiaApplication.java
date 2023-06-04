package com.cognizant.influentia;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class InfluentiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfluentiaApplication.class, args);
	}

	@Bean
    public ModelMapper getModelMapper() {
    	return new ModelMapper();
    }
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}