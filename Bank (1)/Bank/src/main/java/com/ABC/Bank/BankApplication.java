package com.ABC.Bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}
	@Configuration
	public class WebConfig implements WebMvcConfigurer {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**")
					.allowedOrigins("http://localhost:4200") // Update this with your Angular frontend's URL
					.allowedMethods("GET", "POST", "OPTIONS", "DELETE")
					.allowedHeaders("*")
					.allowCredentials(true);
		}
	}


}
