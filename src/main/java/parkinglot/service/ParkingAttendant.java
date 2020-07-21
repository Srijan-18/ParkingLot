package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;
import parkinglot.observer.Owner;

import java.util.Map;

public class ParkingAttendant {
    private Owner owner;
    public ParkingAttendant() {
        owner = new Owner();
    }

    public Map<Integer, String> parkTheCar(String carNumber, Map<Integer, String> parkedCars) {
        if(this.isCarPresent(carNumber, parkedCars))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.CAR_ALREADY_PARKED,
                    "CAR ALREADY PARKED " + carNumber );
        Integer slot = owner.getSlotToPark(parkedCars);
        if(slot == null)
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.PARKING_FULL,
                    "NO MORE SPACE TO PARK " + carNumber );
        parkedCars.put(slot, carNumber);
        return parkedCars;
    }

    public boolean isCarPresent(String carNumber, Map<Integer, String> parkedCars) {
        return parkedCars.containsValue(carNumber);
    }
}