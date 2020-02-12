package pl.markiewicz.teai_4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/cars", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCarList() {
        return new ResponseEntity<>(carService.getCarList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable long id) {
        Optional<Car> findedCarById = carService.getCarById(id);
        if (findedCarById.isPresent()) {
            return new ResponseEntity<>(findedCarById.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getCarListByColor/{color}")
    public ResponseEntity<List<Car>> getCarListByColor(@PathVariable String color) {
        List<Car> findedCarListByColor = carService.getCarList().stream().filter(carModel -> carModel.getColor().
                equals(color)).collect(Collectors.toList());
        if (findedCarListByColor != null && !findedCarListByColor.isEmpty()) {
            return new ResponseEntity<>(findedCarListByColor, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity addCar(@RequestBody Car car) {
        boolean add = carService.getCarList().add(car);
        if (add) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity updateCar(@RequestBody Car updatefullCar) {
        Optional<Car> updatedCar = carService.getCarById(updatefullCar.getId());
        if (updatedCar.isPresent()) {
            carService.getCarList().remove(updatedCar.get());
            carService.getCarList().add(updatefullCar);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity updatePartCar(@PathVariable long id, @RequestBody Car updateCar) {
        Optional<Car> updatePart = carService.getCarById(id);
        if (updatePart.isPresent()) {
            if (updateCar.getColor() != null)
                updatePart.get().setColor(updateCar.getColor());
            if (updateCar.getMark() != null)
                updatePart.get().setMark(updateCar.getMark());
            if (updateCar.getModel() != null)
                updatePart.get().setModel(updateCar.getModel());

            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Car> removeCar(@PathVariable long id) {
        Optional<Car> removeCar = carService.getCarById(id);
        if (removeCar.isPresent()) {
            carService.getCarList().remove(removeCar.get());
            return new ResponseEntity<>(removeCar.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
