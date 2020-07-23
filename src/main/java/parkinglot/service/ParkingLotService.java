package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;
import parkinglot.utility.ParkingUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotService {

    private List<Slot> parkedCars;
    private int parkingLotSize;
    private List<IAuthority> observerList;

    public ParkingLotService(int parkingLotSize) {
        this.parkingLotSize = parkingLotSize;
        parkedCars = new ArrayList<>(Collections.nCopies(this.parkingLotSize, null));
        observerList = new ArrayList<>();
    }

    public void parkTheVehicle(Object vehicle) {
        if (this.checkParkingLotStatus())
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.PARKING_FULL,
                    "NO MORE SPACE TO PARK ");
        if (isVehiclePresent(vehicle))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_ALREADY_PARKED,
                    "GIVEN VEHICLE IS ALREADY PRESENT IN PARKING LOT.");
        parkedCars.set(this.generateParkingSlotToPark(), new Slot(vehicle, new ParkingUtility().getCurrentDateTime()));
        if (checkParkingLotStatus())
            this.notifyObservers();
    }

    public boolean isVehiclePresent(Object vehicle) {
        return parkedCars.stream().anyMatch(slot -> slot != null && slot.getVehicle() == vehicle);
    }

    public void unParkTheVehicle(Object vehicle) {
        if (!isVehiclePresent(vehicle))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT,
                    "GIVEN VEHICLE IS NOT PRESENT IN PARKING LOT.");
        parkedCars.stream().filter(slot -> slot != null && slot.getVehicle() == vehicle)
                .forEach(slot -> parkedCars.set(parkedCars.indexOf(slot), null));
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
                .filter(index -> parkedCars.get(index) == null)
                .findFirst().orElse(-1);
    }

    public int getSlotOfParkedVehicle(Object vehicle) {
        for (Slot slot : parkedCars) {
            if (slot != null && slot.getVehicle() == vehicle) {
                return parkedCars.indexOf(slot) + 1;
            }
        }
        throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT,
                "VEHICLE NOT PRESENT");
    }

    public String getTimeOfParkingForVehicle(Object vehicle) {
        for (Slot slot : parkedCars) {
            if (slot != null && slot.getVehicle() == vehicle)
                return slot.getCurrentDateTime();
        }
        throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT,
                "VEHICLE NOT PRESENT IN PARKING LOT.");
    }
}