package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
public class NativeApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(NativeApplication.class);

	@PostConstruct
	public void onStartup(){
		LOGGER.info("Service startup");
	}

	@PreDestroy
	public void onShutdown() {
		LOGGER.info("Service shutdown");
	}

	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}

	public static void main(String[] args) {
		SpringApplication.run(NativeApplication.class, args);
	}

}
