package ru.mtuci.license_service.servicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.license_service.models.orm.LicenseType;
import ru.mtuci.license_service.models.orm.Product;
import ru.mtuci.license_service.models.rest.request.UpdateLicenseType;
import ru.mtuci.license_service.models.rest.request.UpdateProduct;
import ru.mtuci.license_service.repositories.ProductRepository;
import ru.mtuci.license_service.utils.LicenseServiceException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public Product updateProduct(UpdateProduct productData) throws Exception {
        Optional<Product> product = repository.findById(productData.getId());

        if (product.isEmpty()) {
            throw new LicenseServiceException("Product not found");
        }

        Product confurmedProduct = product.get();
        confurmedProduct.setName(productData.getName());
        confurmedProduct.setBlocked(Objects.equals(productData.getIsBlocked(), "True"));

        Product updatedProduct = repository.save(confurmedProduct);
        repository.flush();
        return updatedProduct;
    }

    public List<Product> getProducts() {
        return repository.findAllProducts();
    }
}
