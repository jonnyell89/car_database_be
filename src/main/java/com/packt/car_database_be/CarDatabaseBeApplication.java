package com.packt.car_database_be;

import com.packt.car_database_be.domain.Car;
import com.packt.car_database_be.domain.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarDatabaseBeApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(
			CarDatabaseBeApplication.class
	);

	private final CarRepository carRepository; // Dependency injection.

	public CarDatabaseBeApplication(CarRepository carRepository) {
		this.carRepository = carRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(CarDatabaseBeApplication.class, args);
		logger.info("Application started :");
	}

	@Override
	public void run(String... args) throws Exception { // CommandLineRunner abstract method.
		carRepository.save(new Car("Ford", "Mustang", "Red", "ADF-1121", 2023, 59000));
		carRepository.save(new Car("Nissan", "Leaf", "White", "SSJ-3002", 2020, 29000));
		carRepository.save(new Car("Toyota", "Prius", "Silver", "KKO-0212", 2022, 39000));

		for (Car car : carRepository.findAll()) {
			logger.info("brand: {}, model: {}", car.getBrand(), car.getModel());
		}
	}
}
