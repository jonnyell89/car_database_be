package com.packt.car_database_be;

import com.packt.car_database_be.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class CarDatabaseBeApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(
			CarDatabaseBeApplication.class
	);

	private final CarRepository carRepository; // Dependency injection.
	private final OwnerRepository ownerRepository; // Dependency injection.
	private final UserRepository userRepository;

	public CarDatabaseBeApplication(OwnerRepository ownerRepository, CarRepository carRepository, UserRepository userRepository) {
		this.ownerRepository = ownerRepository;
		this.carRepository = carRepository;
		this.userRepository = userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(CarDatabaseBeApplication.class, args);
		logger.info("Application started :");
	}

	@Override
	public void run(String... args) throws Exception { // CommandLineRunner abstract method.
		Owner owner1 = new Owner("John", "Johnson");
		Owner owner2 = new Owner("Mary", "Robinson");
		ownerRepository.saveAll(Arrays.asList(owner1, owner2));

		carRepository.save(new Car("Ford", "Mustang", "Red", "ADF-1121", 2023, 59000, owner1));
		carRepository.save(new Car("Nissan", "Leaf", "White", "SSJ-3002", 2020, 29000, owner2));
		carRepository.save(new Car("Toyota", "Prius", "Silver", "KKO-0212", 2022, 39000, owner2));

		for (Car car : carRepository.findAll()) {
			logger.info("brand: {}, model: {}", car.getBrand(), car.getModel());
		}

		// Username: admin, password: admin
		userRepository.save(new User("admin", "$2a$12$Y5zNvfgv3s5Maw3HMIRDK.HzBgu6r15v0N5NWoB3s9xD08iOFQPA2", "ADMIN"));

		// Username: user, password: user
		userRepository.save(new User("user", "$2a$12$3euH0ZIyCuQAUx.872c.I.P5x8RcMm2iMwHxz3W5ZK9JNR7TEdGi2", "USER"));
	}
}
