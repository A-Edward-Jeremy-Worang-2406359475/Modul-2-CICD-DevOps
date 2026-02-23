package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/create")
    public String createProductPage(final Model model) {
        final Product product = new Product();
        model.addAttribute("product", product);
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute final Product product, final Model model) {
        productService.create(product);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String productListPage(final Model model) {
        final List<Product> allProducts = productService.findAll();
        model.addAttribute("products", allProducts);
        return "ProductList";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable("id") final String productId, final Model model) {
        final Product product = productService.findById(productId);

        final String viewName;
        if (product == null) {
            viewName = "redirect:/product/list";
        } else {
            model.addAttribute("product", product);
            viewName = "EditProduct";
        }

        return viewName;
    }

    @PostMapping("/edit")
    public String editProductPost(@ModelAttribute final Product product) {
        productService.update(product);
        return "redirect:/product/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") final String productId) {
        productService.deleteById(productId);
        return "redirect:/product/list";
    }
}