package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryEditDeleteTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }

    private Product makeProduct(String id, String name, int qty) {
        Product p = new Product();
        p.setProductId(id);
        p.setProductName(name);
        p.setProductQuantity(qty);
        return p;
    }


    @Test
    void update_existingProduct_updatesStoredProduct() {

        Product original = makeProduct("p-1", "Shampoo A", 10);
        productRepository.create(original);


        Product updated = makeProduct("p-1", "Shampoo A (Edited)", 99);
        productRepository.update(updated);


        Product fromRepo = productRepository.findById("p-1");
        assertNotNull(fromRepo);
        assertEquals("p-1", fromRepo.getProductId());
        assertEquals("Shampoo A (Edited)", fromRepo.getProductName());
        assertEquals(99, fromRepo.getProductQuantity());
    }

    @Test
    void update_nonExistingProduct_doesNothing() {

        Product original = makeProduct("p-1", "Shampoo A", 10);
        productRepository.create(original);


        Product updated = makeProduct("p-999", "Does Not Exist", 123);
        productRepository.update(updated);

        Product fromRepo = productRepository.findById("p-1");
        assertNotNull(fromRepo);
        assertEquals("Shampoo A", fromRepo.getProductName());
        assertEquals(10, fromRepo.getProductQuantity());

        assertNull(productRepository.findById("p-999"));
    }


    @Test
    void delete_existingProduct_removesProductAndReturnsTrue() {

        Product p1 = makeProduct("p-1", "Shampoo A", 10);
        Product p2 = makeProduct("p-2", "Shampoo B", 20);
        productRepository.create(p1);
        productRepository.create(p2);


        boolean deleted = productRepository.deleteById("p-1");


        assertTrue(deleted);
        assertNull(productRepository.findById("p-1"));
        assertNotNull(productRepository.findById("p-2"));
    }

    @Test
    void delete_nonExistingProduct_returnsFalseAndKeepsData() {

        Product p1 = makeProduct("p-1", "Shampoo A", 10);
        productRepository.create(p1);


        boolean deleted = productRepository.deleteById("p-999");


        assertFalse(deleted);
        assertNotNull(productRepository.findById("p-1"));
        assertNull(productRepository.findById("p-999"));
    }
}
