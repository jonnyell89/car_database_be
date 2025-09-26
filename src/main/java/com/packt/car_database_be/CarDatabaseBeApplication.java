package com.packt.car_database_be;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarDatabaseBeApplication {

	private static final Logger logger = LoggerFactory.getLogger(
			CarDatabaseBeApplication.class
	);

	public static void main(String[] args) {
		SpringApplication.run(CarDatabaseBeApplication.class, args);
		logger.info("Application started: ");
	}
}
