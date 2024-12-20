package ru.mtuci.license_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mtuci.license_service.models.orm.Product;


import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    @Query(value = "SELECT * FROM product", nativeQuery = true)
    List<Product> findAllProducts();
}
