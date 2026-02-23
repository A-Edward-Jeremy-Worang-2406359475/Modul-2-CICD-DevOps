package id.ac.ui.cs.advprog.eshop.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homePageRendersHomePageWithGoToProductListButton() throws Exception {
        final String responseBody = mockMvc.perform(get("/"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final boolean isSuccessful =
                responseBody.contains("Welcome")
                        && responseBody.contains("Go to Product List")
                        && responseBody.contains("/product/list");

        assertTrue(isSuccessful, "Home page should contain Welcome + button + link to /product/list");
    }
}