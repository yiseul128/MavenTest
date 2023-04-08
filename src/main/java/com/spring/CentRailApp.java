package com.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CentRailApp {

	public static void main(String[] args) {
		SpringApplication.run(CentRailApp.class, args);
		System.out.println("App initialized");
	}

}
