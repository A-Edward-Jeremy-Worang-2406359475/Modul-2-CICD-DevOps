package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71afa6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void productHasExpectedId() {
        final boolean isSuccessful =
                "eb558e9f-1c39-460e-8860-71afa6af63bd6".equals(product.getProductId());

        assertTrue(isSuccessful, "Product ID should match the value set in setup");
    }

    @Test
    void productHasExpectedName() {
        final boolean isSuccessful =
                "Sampo Cap Bambang".equals(product.getProductName());

        assertTrue(isSuccessful, "Product name should match the value set in setup");
    }

    @Test
    void productHasExpectedQuantity() {
        final boolean isSuccessful = product.getProductQuantity() == 100;
        assertTrue(isSuccessful, "Product quantity should match the value set in setup");
    }
}