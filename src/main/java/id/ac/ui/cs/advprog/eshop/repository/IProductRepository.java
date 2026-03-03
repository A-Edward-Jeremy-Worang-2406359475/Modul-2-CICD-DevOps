package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    Product save(Product product);
    List<Product> findAll();
    Optional<Product> findById(String productId);
    void update(Product product);
    boolean deleteById(String productId);
}