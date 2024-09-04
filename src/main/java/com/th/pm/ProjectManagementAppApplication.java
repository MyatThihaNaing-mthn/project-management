package com.th.pm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.Validator;

@SpringBootApplication
public class ProjectManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementAppApplication.class, args);
	}

	@Bean
	LocalValidatorFactoryBean validator(){
		return new LocalValidatorFactoryBean();
	}

	@Bean
	Validator getValidator(){
		return new LocalValidatorFactoryBean();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
		return authConfig.getAuthenticationManager();
	}
}
