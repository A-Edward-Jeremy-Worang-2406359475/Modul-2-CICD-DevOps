package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CarRepository implements ICarRepository {

    private final CarIdGenerator idGenerator;
    private final List<Car> carData = new ArrayList<>();

    public CarRepository(CarIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public Car save(Car car) {
        if (car.getCarId() == null) {
            car.setCarId(idGenerator.generate());
        }
        carData.add(car);
        return car;
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(carData);
    }

    @Override
    public Optional<Car> findById(String id) {
        return carData.stream()
                .filter(car -> car.getCarId().equals(id))
                .findFirst();
    }

    @Override
    public Car update(String id, Car updatedCar) {
        Car existing = findById(id).orElse(null);
        if (existing == null) return null;

        existing.setCarName(updatedCar.getCarName());
        existing.setCarColor(updatedCar.getCarColor());
        existing.setCarQuantity(updatedCar.getCarQuantity());
        return existing;
    }

    @Override
    public void deleteById(String id) {
        carData.removeIf(car -> car.getCarId().equals(id));
    }
}