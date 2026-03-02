package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/car")
public class CarController {
    private final CarService carService;

    public CarController(final CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/create")
    public String createCarPage(final Model model) {
        final Car car = new Car();
        model.addAttribute("car", car);
        return "CreateCar";
    }

    @PostMapping("/create")
    public String createCarPost(@ModelAttribute final Car car, final Model model) {
        carService.create(car);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String carListPage(final Model model) {
        final List<Car> allCars = carService.findAll();
        model.addAttribute("cars", allCars);
        return "CarList";
    }

    @GetMapping("/edit/{id}")
    public String editCarPage(@PathVariable("id") final String carId, final Model model) {
        final Car car = carService.findById(carId);

        final String viewName;
        if (car == null) {
            viewName = "redirect:/car/list";
        } else {
            model.addAttribute("car", car);
            viewName = "EditCar";
        }

        return viewName;
    }

    @PostMapping("/edit")
    public String editCarPost(@ModelAttribute final Car car) {
        carService.update(car.getCarId(), car);;
        return "redirect:/car/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteCar(@PathVariable("id") final String carId) {
        carService.deleteCarById(carId);
        return "redirect:/car/list";
    }
}