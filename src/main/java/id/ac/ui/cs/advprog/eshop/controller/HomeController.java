package id.ac.ui.cs.advprog.eshop.controller;
// attempt 4 fixing deployment
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final String homeView;

    public HomeController() {
        this.homeView = "HomePage";
    }

    @GetMapping("/")
    public String homePage() {
        return homeView;
    }
}
