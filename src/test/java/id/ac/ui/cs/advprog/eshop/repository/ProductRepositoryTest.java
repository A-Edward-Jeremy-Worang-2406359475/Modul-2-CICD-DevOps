package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductRepositoryTest {

    private final ProductRepository productRepository = new ProductRepository();

    @Test
    void testSaveAndFindAll() {
        final Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71afa6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        productRepository.save(product);

        final List<Product> list = productRepository.findAll();
        final boolean isSuccessful =
                !list.isEmpty()
                        && product.getProductId().equals(list.get(0).getProductId());

        assertTrue(isSuccessful, "Saved product should appear in repository list");
    }

    @Test
    void testFindAllIfEmpty() {
        final List<Product> list = productRepository.findAll();
        final boolean isSuccessful = list.isEmpty();

        assertTrue(isSuccessful, "Empty repository should return empty list");
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        final Product product1 = new Product();
        product1.setProductId("id-1");
        product1.setProductName("P1");
        product1.setProductQuantity(100);
        productRepository.save(product1);

        final Product product2 = new Product();
        product2.setProductId("id-2");
        product2.setProductName("P2");
        product2.setProductQuantity(50);
        productRepository.save(product2);

        final List<Product> list = productRepository.findAll();
        final boolean isSuccessful =
                list.size() == 2
                        && "id-1".equals(list.get(0).getProductId())
                        && "id-2".equals(list.get(1).getProductId());

        assertTrue(isSuccessful, "List should return both products in insertion order");
    }
}