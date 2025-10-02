package com.packt.car_database_be.web;

import com.packt.car_database_be.domain.Car;
import com.packt.car_database_be.domain.CarRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // CREATE
    @PostMapping
    public Car createCar(@RequestBody Car car) {
        return carRepository.save(car);
    }

    // READ
    @GetMapping
    public Iterable<Car> getCars() {
        return carRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car updatedCar) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setBrand(updatedCar.getBrand());
                    car.setModel(updatedCar.getModel());
                    car.setColour(updatedCar.getColour());
                    car.setRegistrationNumber(updatedCar.getRegistrationNumber());
                    car.setModelYear(updatedCar.getModelYear());
                    car.setPrice(updatedCar.getPrice());
                    car.setOwner(updatedCar.getOwner());
                    return ResponseEntity.ok(carRepository.save(car));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Car> patchCar(@PathVariable Long id, @RequestBody Car partialCar) {
        return carRepository.findById(id)
                .map(car -> {
                    if (partialCar.getBrand() != null) car.setBrand(partialCar.getBrand());
                    if (partialCar.getModel() != null) car.setModel(partialCar.getModel());
                    if (partialCar.getColour() != null) car.setColour(partialCar.getColour());
                    if (partialCar.getRegistrationNumber() != null) car.setRegistrationNumber(partialCar.getRegistrationNumber());
                    if (partialCar.getModelYear() != null) car.setModelYear(partialCar.getModelYear());
                    if (partialCar.getPrice() != null) car.setPrice(partialCar.getPrice());
                    if (partialCar.getOwner() != null) car.setOwner(partialCar.getOwner());
                    return ResponseEntity.ok(carRepository.save(car));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        return carRepository.findById(id)
                .map(car -> {
                    carRepository.delete(car);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
