package com.maticz.Karting.results.API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KartingResultsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KartingResultsApiApplication.class, args);
	}

}
