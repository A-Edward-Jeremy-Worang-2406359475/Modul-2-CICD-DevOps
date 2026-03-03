package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository implements IProductRepository {

    private final List<Product> productData;

    public ProductRepository() {
        this.productData = new ArrayList<>();
    }

    @Override
    public Product save(final Product product) {
        productData.add(product);
        return product;
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productData);
    }

    @Override
    public Optional<Product> findById(final String productId) {
        for (final Product product : productData) {
            if (product.getProductId().equals(productId)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(final Product updated) {
        for (int i = 0; i < productData.size(); i++) {
            if (productData.get(i).getProductId().equals(updated.getProductId())) {
                productData.set(i, updated);
                return;
            }
        }
    }

    @Override
    public boolean deleteById(final String productId) {
        for (int i = 0; i < productData.size(); i++) {
            if (productData.get(i).getProductId().equals(productId)) {
                productData.remove(i);
                return true;
            }
        }
        return false;
    }
}