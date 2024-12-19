package ru.mtuci.license_service.servicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.license_service.models.orm.Product;
import ru.mtuci.license_service.repositories.ProductRepository;
import ru.mtuci.license_service.utils.LicenseServiceException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public Product createProduct(String productName) throws Exception {

        if (repository.findByName(productName).isPresent()) {
            throw new LicenseServiceException("Product already exists");
        }

        Product newProduct = new Product();
        newProduct.setBlocked(false);
        newProduct.setName(productName);

        Product createdProduct = repository.save(newProduct);
        repository.flush();

        return createdProduct;
    }

    public List<Product> getProducts() {
        return repository.findAllProducts();
    }
}
