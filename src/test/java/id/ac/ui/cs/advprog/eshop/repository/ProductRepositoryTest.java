package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    private ProductRepository productRepository;

    @Test
    void testCreateAndFind() {
        final Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71afa6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        productRepository.create(product);

        final Iterator<Product> iterator = productRepository.findAll();
        final boolean isSuccessful =
                iterator.hasNext()
                        && product.getProductId().equals(iterator.next().getProductId());

        assertTrue(isSuccessful, "Created product should appear in repository iterator");
    }

    @Test
    void testFindAllIfEmpty() {
        final Iterator<Product> iterator = productRepository.findAll();
        final boolean isSuccessful = !iterator.hasNext();

        assertTrue(isSuccessful, "Empty repository should return iterator with no elements");
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        final Product product1 = new Product();
        product1.setProductId("id-1");
        product1.setProductName("P1");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        final Product product2 = new Product();
        product2.setProductId("id-2");
        product2.setProductName("P2");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        final Iterator<Product> iterator = productRepository.findAll();
        final boolean isSuccessful =
                iterator.hasNext()
                        && "id-1".equals(iterator.next().getProductId())
                        && iterator.hasNext()
                        && "id-2".equals(iterator.next().getProductId())
                        && !iterator.hasNext();

        assertTrue(isSuccessful, "Iterator should return both products in insertion order");
    }
}