package pl.markiewicz.teai_4;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    public List<Car> carList = new ArrayList<>();

    CarService() {
        carList.add(new Car(1L, "Opel", "Astra", "blue"));
        carList.add(new Car(2L, "Mazda", "2", "red"));
        carList.add(new Car(3L, "BMW", "i5", "black"));
    }

    public CarService(List<Car> carList) {
        this.carList = carList;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public Optional<Car> getCarById(long id) {
        return getCarList().stream().filter(car -> car.getId() == id).findFirst();
    }

    public void addCar(Car car) {
        carList.add(car);
    }
}
