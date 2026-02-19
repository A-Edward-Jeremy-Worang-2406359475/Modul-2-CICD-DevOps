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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void create_whenIdNull_generatesIdAndCallsRepo() {
        Product p = new Product();
        p.setProductName("A");
        p.setProductQuantity(1);
        p.setProductId(null);

        service.create(p);

        assertNotNull(p.getProductId());
        assertFalse(p.getProductId().isBlank());

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).create(captor.capture());
        assertEquals(p.getProductId(), captor.getValue().getProductId());
    }

    @Test
    void create_whenIdProvided_keepsId() {
        Product p = new Product();
        p.setProductId("fixed-id");
        p.setProductName("A");
        p.setProductQuantity(1);

        service.create(p);

        assertEquals("fixed-id", p.getProductId());
        verify(productRepository).create(p);
    }

    @Test
    void findAll_returnsListFromIterator() {
        Product p1 = new Product(); p1.setProductId("1");
        Product p2 = new Product(); p2.setProductId("2");
        Iterator<Product> it = Arrays.asList(p1, p2).iterator();

        when(productRepository.findAll()).thenReturn(it);

        List<Product> res = service.findAll();

        assertEquals(2, res.size());
        assertEquals("1", res.get(0).getProductId());
        assertEquals("2", res.get(1).getProductId());
        verify(productRepository).findAll();
    }

    @Test
    void update_callsRepositoryUpdate() {
        Product p = new Product();
        p.setProductId("1");

        service.update(p);

        verify(productRepository).update(p);
    }

    @Test
    void deleteById_returnsRepositoryResult() {
        when(productRepository.deleteById("1")).thenReturn(true);

        boolean result = service.deleteById("1");

        assertTrue(result);
        verify(productRepository).deleteById("1");
    }
}
