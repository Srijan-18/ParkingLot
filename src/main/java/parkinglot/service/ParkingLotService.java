package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;

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
        if(isParkingLotFull)
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.PARKING_FULL,
                                                 "NO MORE SPACE TO PARK " + carNumber );
        parkedCars.put(carNumber, carNumber);
        if(parkedCars.size() == parkingLotSize)
            isParkingLotFull = true;
    }

    public boolean isCarPresent(String carNumber) {
        return parkedCars.containsKey(carNumber);
    }

    public void unParkTheCar(String carNumber) {
        if(!isCarPresent(carNumber))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.CAR_NOT_PRESENT,
                                                 carNumber + " IS NOT PRESENT IN PARKING LOT.");
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