package parkinglot.service;

import java.util.HashMap;
import java.util.Map;

public class ParkingLotService {

    private Map<String, String> parkedCars;
    private int parkingLotSize;
    private boolean isParkingLotFull;

    public ParkingLotService() {
        parkedCars = new HashMap<>();
        isParkingLotFull = false;
    }

    public void parkTheCar(String carNumber) {
        parkedCars.put(carNumber, carNumber);
        if(parkedCars.size() == parkingLotSize)
            isParkingLotFull = true;
    }

    public boolean isCarPresent(String carNumber) {
        return parkedCars.containsKey(carNumber);
    }

    public void unParkTheCar(String carNumber) {
        parkedCars.remove(carNumber);
        isParkingLotFull = false;
    }

    public void setParkingLotSize(int size) {
        this.parkingLotSize = size;
    }

    public boolean checkParkingLotStatus() {
        return isParkingLotFull;
    }
}