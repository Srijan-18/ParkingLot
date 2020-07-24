package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;
import parkinglot.model.Slot;
import parkinglot.utility.ParkingUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotService {
    private static final int INDEX_FACTOR = 1;
    private final int numberOfLots;
    private List<ParkingLot> parkingLots;
    private final int parkingLotSize;
    private List<IAuthority> observerList;

    public ParkingLotService(int singleParkingLotSize, int numberOfLots) {
        this.numberOfLots = numberOfLots;
        this.parkingLots = new ArrayList<>();
        IntStream.range(0, numberOfLots)
                .forEachOrdered(index -> parkingLots.add(index, new ParkingLot(singleParkingLotSize)));
        this.parkingLotSize = singleParkingLotSize;
        observerList = new ArrayList<>();
    }


    public void parkTheVehicle(Object vehicle) {
        if (this.checkParkingLotStatus())
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.PARKING_FULL,
                    "NO MORE SPACE TO PARK ");
        if (isVehiclePresent(vehicle))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_ALREADY_PARKED,
                    "GIVEN VEHICLE IS ALREADY PRESENT IN PARKING LOT.");
        ParkingLot parkingLotToPark = this.getTheLotToPark(parkingLots);
        int slotToPark = this.generateParkingSlotToPark(parkingLotToPark);
        parkingLotToPark.parkedCars.set(slotToPark, new Slot(vehicle, new ParkingUtility().getCurrentDateTime()));
        if (checkParkingLotStatus())
            this.notifyObserversOfFullParkingLot();
    }

    public boolean isVehiclePresent(Object vehicle) {
        return parkingLots.stream().findFirst()
                .filter(parkingLot -> parkingLot.parkedCars.stream()
                        .anyMatch(slot -> slot != null && slot.getVehicle() == vehicle)).isPresent();
    }

    public void unParkTheVehicle(Object vehicle) {
        ParkingLot lotOfVehicle = this.getLotOfParkedVehicle(vehicle);
        lotOfVehicle.parkedCars.stream().filter(slot -> slot != null && slot.getVehicle() == vehicle)
                .forEach(slot -> lotOfVehicle.parkedCars.set(lotOfVehicle.parkedCars.indexOf(slot), null));
        if (!checkParkingLotStatus())
            this.notifyObserversOfAvailability();
    }

    private void notifyObserversOfAvailability() {
        for (IAuthority observer : observerList) {
            observer.spaceAvailableForParking();
        }
    }

    public boolean checkParkingLotStatus() {
        int carsParked = parkingLots.stream().mapToInt(ParkingLot::getNumberOfVehiclesParked).sum();
        return this.numberOfLots * this.parkingLotSize == carsParked;
    }

    public void addObserver(IAuthority authority) {
        this.observerList.add(authority);
    }

    private void notifyObserversOfFullParkingLot() {
        for (IAuthority observer : observerList) {
            observer.fullCapacityReached();
        }
    }

    private int generateParkingSlotToPark(ParkingLot parkingLot) {
        return IntStream.range(0, parkingLot.parkedCars.size())
                .filter(index -> parkingLot.parkedCars.get(index) == null)
                .findFirst().orElse(-1);
    }

    public String getSlotOfParkedVehicle(Object vehicle) {
       ParkingLot parkingLot = this.getLotOfParkedVehicle(vehicle);
       int slotNumberInItsLot =  parkingLot.getSlotOfVehicleParked(vehicle);
        return "P:" + (parkingLots.indexOf(parkingLot) + INDEX_FACTOR) + " S:" + (slotNumberInItsLot + INDEX_FACTOR);
    }

    private ParkingLot getLotOfParkedVehicle(Object vehicle) {
        for (ParkingLot parkingLot : parkingLots)
            for (Slot slot : parkingLot.parkedCars) {
                if (slot != null && slot.getVehicle() == vehicle) {
                    return parkingLot;
                }
            }
        throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT,
                "VEHICLE NOT PRESENT");
    }

    public String getTimeOfParkingForVehicle(Object vehicle) {
        for (ParkingLot parkingLot : parkingLots)
            if (parkingLot.getTimeOfParking(vehicle) != null)
                return parkingLot.getTimeOfParking(vehicle);
        throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT,
                "VEHICLE NOT PRESENT IN PARKING LOT.");
    }

    private ParkingLot getTheLotToPark(List<ParkingLot> parkingLots) {
        List<ParkingLot> parkingLotList = parkingLots;
        Collections.sort(parkingLotList, Comparator.comparing(parkingLot -> parkingLot.getNumberOfVehiclesParked()));
        return parkingLotList.get(0);
    }
}