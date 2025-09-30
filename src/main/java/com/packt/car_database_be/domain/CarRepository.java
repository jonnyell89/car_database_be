package com.packt.car_database_be.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CarRepository extends CrudRepository<Car, Long> {
    List<Car> findByBrand(@Param("brand") String brand);
    List<Car> findByModel(@Param("model") String model);
    List<Car> findByColour(@Param("colour") String colour);
    List<Car> findByRegistrationNumber(@Param("registrationNumber") String registrationNumber);
    List<Car> findByModelYear(@Param("modelYear") int modelYear);
    List<Car> findByPrice(@Param("price") int price);

    List<Car> findByBrandAndModel(@Param("brand") String brand, @Param("model") String model);
    List<Car> findByBrandOrColour(@Param("brand") String brand, @Param("colour") String colour);

    List<Car> findByBrandOrderByModelYearAsc(@Param("brand") String brand);

//    Custom queries make your application less portable across different database systems.
//    @Query("select c from Car c where c.brand = ?1")
//    List<Car> findByBrand(String brand);
//
//    @Query("select c from Car c where c.brand like %?1")
//    List<Car> findByBrandEndsWith(String brand);
}
