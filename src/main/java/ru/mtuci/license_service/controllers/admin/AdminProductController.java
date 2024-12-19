package ru.mtuci.license_service.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.license_service.models.orm.Product;
import ru.mtuci.license_service.models.rest.response.GenericResponse;
import ru.mtuci.license_service.servicies.ProductService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService service;

    @PostMapping("create/{productName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@PathVariable String productName) {
        try {
            Product product = service.createProduct(productName);
            return ResponseEntity.status(HttpStatus.OK).body(product);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getProducts() {
        try {
            List<Product> products = service.getProducts();
            return ResponseEntity.status(HttpStatus.OK).body(products);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }
}
