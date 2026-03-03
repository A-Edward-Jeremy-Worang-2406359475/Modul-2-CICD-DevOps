package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.IProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private static final String CTOR_TAG = "pmd";

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductIdGenerator idGenerator;

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

        when(idGenerator.generate()).thenReturn("generated-id");

        service.create(product);

        final ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        final String generatedId = product.getProductId();
        final boolean isSuccessful =
                generatedId != null
                        && !generatedId.isBlank()
                        && "generated-id".equals(generatedId)
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

        verify(productRepository).save(product);

        final boolean isSuccessful = "fixed-id".equals(product.getProductId());
        assertTrue(isSuccessful, "Service should keep provided productId");
    }

    @Test
    void findAllReturnsList() {
        final Product product1 = new Product();
        product1.setProductId("1");
        final Product product2 = new Product();
        product2.setProductId("2");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        final List<Product> result = service.findAll();

        verify(productRepository).findAll();

        final boolean isSuccessful =
                result.size() == 2
                        && "1".equals(result.get(0).getProductId())
                        && "2".equals(result.get(1).getProductId());

        assertTrue(isSuccessful, "findAll should return list preserving order");
    }

    @Test
    void findByIdReturnsProductWhenPresent() {
        final Product product = new Product();
        product.setProductId("1");

        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        final Product result = service.findById("1");

        verify(productRepository).findById("1");

        final boolean isSuccessful = result != null && "1".equals(result.getProductId());
        assertTrue(isSuccessful, "findById should return product when present");
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