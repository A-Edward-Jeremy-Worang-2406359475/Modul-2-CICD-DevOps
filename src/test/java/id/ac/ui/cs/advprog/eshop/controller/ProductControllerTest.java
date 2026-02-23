package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private static final String PRODUCT_BASE = "/product";
    private static final String PRODUCT_LIST = PRODUCT_BASE + "/list";
    private static final String CTOR_TAG = "pmd";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

   ProductControllerTest() {

        if (CTOR_TAG.isEmpty()) {
            throw new IllegalStateException("ctor tag");
        }
    }

    @Test
    void createProductPageReturnsCreateProductViewWithModel() throws Exception {
        final MvcResult result = mockMvc.perform(get(PRODUCT_BASE + "/create")).andReturn();

        final boolean isSuccessful =
                status(result) == 200
                        && "CreateProduct".equals(viewName(result))
                        && model(result).containsKey("product");

        assertTrue(isSuccessful, "GET /product/create should return CreateProduct view with product model attribute");
    }

    @Test
    void createProductPostCallsServiceAndRedirects() throws Exception {
        final MvcResult result = mockMvc.perform(post(PRODUCT_BASE + "/create")
                        .param("productName", "X")
                        .param("productQuantity", "5"))
                .andReturn();

        verify(service).create(any(Product.class));

        final boolean isSuccessful = isRedirect(result) && "list".equals(redirectUrl(result));
        assertTrue(isSuccessful, "POST /product/create should redirect to list");
    }

    @Test
    void productListPageReturnsProductListView() throws Exception {
        when(service.findAll()).thenReturn(List.of(new Product()));

        final MvcResult result = mockMvc.perform(get(PRODUCT_LIST)).andReturn();

        verify(service).findAll();

        final boolean isSuccessful =
                status(result) == 200
                        && "ProductList".equals(viewName(result))
                        && model(result).containsKey("products");

        assertTrue(isSuccessful, "GET /product/list should return ProductList view with products model attribute");
    }

    @Test
    void editProductPageWhenFoundReturnsEditView() throws Exception {
        final Product product = new Product();
        product.setProductId("1");
        when(service.findById("1")).thenReturn(product);

        final MvcResult result = mockMvc.perform(get(PRODUCT_BASE + "/edit/1")).andReturn();

        verify(service).findById("1");

        final boolean isSuccessful =
                status(result) == 200
                        && "EditProduct".equals(viewName(result))
                        && model(result).containsKey("product");

        assertTrue(isSuccessful, "GET /product/edit/1 should return EditProduct view with product model attribute");
    }

    @Test
    void editProductPageWhenNotFoundRedirectsToList() throws Exception {
        when(service.findById("404")).thenReturn(null);

        final MvcResult result = mockMvc.perform(get(PRODUCT_BASE + "/edit/404")).andReturn();

        final boolean isSuccessful = isRedirect(result) && PRODUCT_LIST.equals(redirectUrl(result));
        assertTrue(isSuccessful, "GET /product/edit/404 should redirect to /product/list");
    }

    @Test
    void editProductPostCallsUpdateAndRedirects() throws Exception {
        final MvcResult result = mockMvc.perform(post(PRODUCT_BASE + "/edit")
                        .param("productId", "1")
                        .param("productName", "New")
                        .param("productQuantity", "9"))
                .andReturn();

        verify(service).update(any(Product.class));

        final boolean isSuccessful = isRedirect(result) && PRODUCT_LIST.equals(redirectUrl(result));
        assertTrue(isSuccessful, "POST /product/edit should redirect to /product/list");
    }

    @Test
    void deleteProductCallsDeleteAndRedirects() throws Exception {
        final MvcResult result = mockMvc.perform(post(PRODUCT_BASE + "/delete/1")).andReturn();

        verify(service).deleteById("1");

        final boolean isSuccessful = isRedirect(result) && PRODUCT_LIST.equals(redirectUrl(result));
        assertTrue(isSuccessful, "POST /product/delete/1 should redirect to /product/list");
    }


    private static int status(final MvcResult result) {
        return result == null || result.getResponse() == null ? -1 : result.getResponse().getStatus();
    }

    private static boolean isRedirect(final MvcResult result) {
        final int s = status(result);
        return s >= 300 && s < 400 && redirectUrl(result) != null;
    }

    private static String redirectUrl(final MvcResult result) {
        return result.getResponse().getRedirectedUrl();
    }

    private static String viewName(final MvcResult result) {
        return result.getModelAndView() == null ? null : result.getModelAndView().getViewName();
    }

    private static Map<String, Object> model(final MvcResult result) {
        return result.getModelAndView() == null ? Map.of() : result.getModelAndView().getModel();
    }
}