package com.example.AlertIT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlertItApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertItApplication.class, args);
	}

}