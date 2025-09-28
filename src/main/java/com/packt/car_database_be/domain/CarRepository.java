package com.packt.car_database_be.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {
    List<Car> findByBrand(String brand);
    List<Car> findByModel(String model);
    List<Car> findByColour(String colour);
    List<Car> findByRegistrationNumber(String registrationNumber);
    List<Car> findByModelYear(int modelYear);
    List<Car> findByPrice(int price);

    List<Car> findByBrandAndModel(String brand, String model);
    List<Car> findByBrandOrColour(String brand, String colour);

    List<Car> findByBrandOrderByModelYearAsc(String brand);

//    Custom queries make your application less portable across different database systems.
//    @Query("select c from Car c where c.brand = ?1")
//    List<Car> findByBrand(String brand);
//
//    @Query("select c from Car c where c.brand like %?1")
//    List<Car> findByBrandEndsWith(String brand);
}
