package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void createProductPage_returnsCreateProductViewWithModel() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void createProductPost_callsServiceAndRedirects() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productName", "X")
                        .param("productQuantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(service).create(any(Product.class));
    }

    @Test
    void productListPage_returnsProductListView() throws Exception {
        when(service.findAll()).thenReturn(List.of(new Product()));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ProductList"))
                .andExpect(model().attributeExists("products"));

        verify(service).findAll();
    }

    @Test
    void editProductPage_whenFound_returnsEditView() throws Exception {
        Product p = new Product();
        p.setProductId("1");
        when(service.findById("1")).thenReturn(p);

        mockMvc.perform(get("/product/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditProduct"))
                .andExpect(model().attributeExists("product"));

        verify(service).findById("1");
    }

    @Test
    void editProductPage_whenNotFound_redirectsToList() throws Exception {
        when(service.findById("404")).thenReturn(null);

        mockMvc.perform(get("/product/edit/404"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    void editProductPost_callsUpdateAndRedirects() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "1")
                        .param("productName", "New")
                        .param("productQuantity", "9"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service).update(any(Product.class));
    }

    @Test
    void deleteProduct_callsDeleteAndRedirects() throws Exception {
        mockMvc.perform(post("/product/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service).deleteById("1");
    }
}
