package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotService {

    private List<Object> parkedCars;
    private int parkingLotSize;
    private List<IAuthority> observerList;

    public ParkingLotService(int parkingLotSize) {
        this.parkingLotSize = parkingLotSize;
        parkedCars = new ArrayList<>();
        IntStream.range(0, this.parkingLotSize).forEachOrdered(slot -> parkedCars.add(slot, null));
        observerList = new ArrayList<>();
    }

    public void parkTheVehicle(Object vehicle) {
        if (this.checkParkingLotStatus())
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.PARKING_FULL,
                    "NO MORE SPACE TO PARK ");
        parkedCars.add(this.generateParkingSlotToPark(), vehicle);
        if (checkParkingLotStatus())
            this.notifyObservers();
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
        return this.parkingLotSize == IntStream.range(0, this.parkingLotSize)
                                               .filter(slot -> parkedCars.get(slot) == null)
                                               .findFirst().orElse(this.parkingLotSize);
    }

    public void addObserver(IAuthority authority) {
        this.observerList.add(authority);
    }

    private void notifyObservers() {
        for (IAuthority observer : observerList) {
            observer.fullCapacityReached(this.checkParkingLotStatus());
        }
    }

    private int generateParkingSlotToPark() {
        return IntStream.range(0, this.parkingLotSize)
                        .filter(slot -> parkedCars.get(slot) == null)
                        .findFirst().orElse(-1);
    }
}