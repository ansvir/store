package com.project.store.service;

import com.project.store.domain.Product;
import com.project.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product changeProduct(Product product, Long id) {
        return productRepository.findById(id)
                .map(productFromRepo -> {
                    productFromRepo.setName(product.getName());
                    productFromRepo.setDescription(product.getDescription());
                    productFromRepo.setTags(product.getTags());
                    return productRepository.save(productFromRepo);
                })
                .orElseGet(() -> {
                    product.setId(id);
                    return productRepository.save(product);
                });
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
