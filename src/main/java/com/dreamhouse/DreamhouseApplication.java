package com.dreamhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DreamhouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(DreamhouseApplication.class, args);
	}

}