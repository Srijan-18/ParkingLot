package parkinglot.service;

import java.util.HashMap;
import java.util.Map;

public class ParkingLotService {

    private Map<String, String> parkedCars;

    public ParkingLotService() {
        parkedCars = new HashMap<>();
    }

    public void parkTheCar(String carNumber) {
        parkedCars.put(carNumber, carNumber);
    }

    public boolean isCarPresent(String carNumber) {
        return parkedCars.containsKey(carNumber);
    }

    public void unParkTheCar(String carNumber) {
        parkedCars.remove(carNumber);
    }
}