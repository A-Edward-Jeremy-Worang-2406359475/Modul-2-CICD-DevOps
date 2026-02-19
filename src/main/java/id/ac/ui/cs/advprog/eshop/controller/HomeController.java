package id.ac.ui.cs.advprog.eshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    public HomeController() {
        // explicit no-args constructor for PMD
    }

    @GetMapping("/")
    public String homePage() {
        return "HomePage";
    }
}
