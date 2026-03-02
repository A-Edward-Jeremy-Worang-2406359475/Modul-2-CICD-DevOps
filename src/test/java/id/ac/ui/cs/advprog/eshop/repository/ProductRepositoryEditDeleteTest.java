package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductRepositoryEditDeleteTest {

    private static final String PRODUCT_ID_1 = "p-1";
    private static final String PRODUCT_ID_2 = "p-2";
    private static final String PRODUCT_ID_MISSING = "p-999";

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }

    private Product makeProduct(final String productId, final String name, final int quantity) {
        final Product product = new Product();
        product.setProductId(productId);
        product.setProductName(name);
        product.setProductQuantity(quantity);
        return product;
    }

    @Test
    void updateExistingProductUpdatesStoredProduct() {
        final Product original = makeProduct(PRODUCT_ID_1, "Shampoo A", 10);
        productRepository.create(original);

        final Product updated = makeProduct(PRODUCT_ID_1, "Shampoo A (Edited)", 99);
        productRepository.update(updated);

        final Product fromRepo = productRepository.findById(PRODUCT_ID_1);
        final boolean isSuccessful =
                fromRepo != null
                        && PRODUCT_ID_1.equals(fromRepo.getProductId())
                        && "Shampoo A (Edited)".equals(fromRepo.getProductName())
                        && fromRepo.getProductQuantity() == 99;

        assertTrue(isSuccessful, "Update should replace stored product fields for existing ID");
    }

    @Test
    void updateNonExistingProductDoesNothing() {
        final Product original = makeProduct(PRODUCT_ID_1, "Shampoo A", 10);
        productRepository.create(original);

        final Product updated = makeProduct(PRODUCT_ID_MISSING, "Does Not Exist", 123);
        productRepository.update(updated);

        final Product fromRepo = productRepository.findById(PRODUCT_ID_1);
        final Product missing = productRepository.findById(PRODUCT_ID_MISSING);

        final boolean isSuccessful =
                fromRepo != null
                        && "Shampoo A".equals(fromRepo.getProductName())
                        && fromRepo.getProductQuantity() == 10
                        && missing == null;

        assertTrue(isSuccessful, "Update should not add new product if ID does not exist");
    }

    @Test
    void deleteExistingProductRemovesProductAndReturnsTrue() {
        final Product product1 = makeProduct(PRODUCT_ID_1, "Shampoo A", 10);
        final Product product2 = makeProduct(PRODUCT_ID_2, "Shampoo B", 20);
        productRepository.create(product1);
        productRepository.create(product2);

        final boolean deleted = productRepository.deleteById(PRODUCT_ID_1);

        final Product shouldBeMissing = productRepository.findById(PRODUCT_ID_1);
        final Product shouldRemain = productRepository.findById(PRODUCT_ID_2);

        final boolean isSuccessful =
                deleted && shouldBeMissing == null && shouldRemain != null;

        assertTrue(isSuccessful, "Delete should remove existing product and keep other data intact");
    }

    @Test
    void deleteNonExistingProductReturnsFalseAndKeepsData() {
        final Product product1 = makeProduct(PRODUCT_ID_1, "Shampoo A", 10);
        productRepository.create(product1);

        final boolean deleted = productRepository.deleteById(PRODUCT_ID_MISSING);

        final Product stillThere = productRepository.findById(PRODUCT_ID_1);
        final Product missing = productRepository.findById(PRODUCT_ID_MISSING);

        final boolean isSuccessful =
                !deleted && stillThere != null && missing == null;

        assertTrue(isSuccessful, "Delete should return false for missing ID and keep existing data");
    }
}