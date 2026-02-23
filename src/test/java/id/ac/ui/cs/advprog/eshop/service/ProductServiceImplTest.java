package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private static final String CTOR_TAG = "pmd";

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl service;

    ProductServiceImplTest() {
        if (CTOR_TAG.isEmpty()) {
            throw new IllegalStateException("ctor tag");
        }
    }

    @Test
    void createWhenIdNullGeneratesIdAndCallsRepo() {
        final Product product = new Product();
        product.setProductName("A");
        product.setProductQuantity(1);
        product.setProductId(null);

        service.create(product);

        final ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).create(captor.capture());

        final String generatedId = product.getProductId();
        final boolean isSuccessful =
                generatedId != null
                        && !generatedId.isBlank()
                        && generatedId.equals(captor.getValue().getProductId());

        assertTrue(isSuccessful, "Service should generate ID and pass same ID to repository");
    }

    @Test
    void createWhenIdProvidedKeepsId() {
        final Product product = new Product();
        product.setProductId("fixed-id");
        product.setProductName("A");
        product.setProductQuantity(1);

        service.create(product);

        verify(productRepository).create(product);

        final boolean isSuccessful = "fixed-id".equals(product.getProductId());
        assertTrue(isSuccessful, "Service should keep provided productId");
    }

    @Test
    void findAllReturnsListFromIterator() {
        final Product product1 = new Product();
        product1.setProductId("1");
        final Product product2 = new Product();
        product2.setProductId("2");

        final Iterator<Product> iterator = Arrays.asList(product1, product2).iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        final List<Product> result = service.findAll();

        verify(productRepository).findAll();

        final boolean isSuccessful =
                result.size() == 2
                        && "1".equals(result.get(0).getProductId())
                        && "2".equals(result.get(1).getProductId());

        assertTrue(isSuccessful, "findAll should convert iterator to list preserving order");
    }

    @Test
    void updateCallsRepositoryUpdate() {
        final Product product = new Product();
        product.setProductId("1");

        service.update(product);

        verify(productRepository).update(product);

        final boolean isSuccessful = "1".equals(product.getProductId());
        assertTrue(isSuccessful, "update should not change productId");
    }

    @Test
    void deleteByIdReturnsRepositoryResult() {
        when(productRepository.deleteById("1")).thenReturn(true);

        final boolean result = service.deleteById("1");

        verify(productRepository).deleteById("1");

        assertTrue(result, "deleteById should return repository result");
    }
}