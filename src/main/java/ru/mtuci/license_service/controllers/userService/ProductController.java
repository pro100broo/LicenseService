package ru.mtuci.license_service.controllers.userService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.license_service.models.orm.Product;
import ru.mtuci.license_service.models.rest.response.GenericResponse;
import ru.mtuci.license_service.servicies.ProductService;

import java.util.List;

//TODO: 1. Добавить операций

@RestController
@RequestMapping("/api/v1/userService/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

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
