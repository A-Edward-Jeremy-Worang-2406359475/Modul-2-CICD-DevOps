package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepository {

    private final List<Product> productData;


    public ProductRepository() {
        this.productData = new ArrayList<>();
    }

    public Product create(final Product product) {
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(final String productId) {
        Product found = null;

        for (final Product product : productData) {
            if (product.getProductId().equals(productId)) {
                found = product;
                break;
            }
        }

        return found;
    }

    public void update(final Product updated) {
        for (int i = 0; i < productData.size(); i++) {
            if (productData.get(i).getProductId().equals(updated.getProductId())) {
                productData.set(i, updated);
                break;
            }
        }
    }

    public boolean deleteById(final String productId) {
        boolean deleted = false;

        for (int i = 0; i < productData.size(); i++) {
            if (productData.get(i).getProductId().equals(productId)) {
                productData.remove(i);
                deleted = true;
                break;
            }
        }

        return deleted;
    }
}