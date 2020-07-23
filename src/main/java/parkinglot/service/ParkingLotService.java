package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotService {

    private List<Object> parkedCars;
    private int parkingLotSize;

    public ParkingLotService(int parkingLotSize) {
        this.parkingLotSize = parkingLotSize;
        parkedCars = new ArrayList<>(this.parkingLotSize);
    }

    public void parkTheVehicle(Object vehicle) {
        if (this.checkParkingLotStatus())
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.PARKING_FULL,
                    "NO MORE SPACE TO PARK ");
        parkedCars.add(vehicle);
    }

    public boolean isVehiclePresent(Object vehicle) {
        return parkedCars.contains(vehicle);
    }

    public void unParkTheVehicle(Object vehicle) {
        if (!isVehiclePresent(vehicle))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.CAR_NOT_PRESENT,
                    "GIVEN VEHICLE IS NOT PRESENT IN PARKING LOT.");
        parkedCars.remove(vehicle);
    }

    public boolean checkParkingLotStatus() {
        return this.parkingLotSize == this.parkedCars.size();
    }
}