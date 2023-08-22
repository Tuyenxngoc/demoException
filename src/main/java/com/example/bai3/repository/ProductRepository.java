package com.example.bai3.repository;

import com.example.bai3.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);

    boolean existsByName(String name);

    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    @Query(value = "SELECT * FROM product WHERE price BETWEEN :minPrice AND :maxPrice", nativeQuery = true)
    List<Product> findProductsInPriceRange(@Param("minPrice") double min, @Param("maxPrice") double max);

    @Query(value = "SELECT * FROM product WHERE price < :maxPrice", nativeQuery = true)
    List<Product> findCheapProducts(@Param("maxPrice") double maxPrice);

}
