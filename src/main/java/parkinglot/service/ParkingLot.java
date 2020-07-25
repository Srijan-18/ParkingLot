package parkinglot.service;


import parkinglot.model.Slot;
import parkinglot.model.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLot {
    public List<Slot> parkedCars;

    public ParkingLot(int parkingLotSize) {
        parkedCars = new ArrayList<>(Collections.nCopies(parkingLotSize, null));
    }

    public int getNumberOfVehiclesParked() {
        return (int) IntStream.range(0, parkedCars.size())
                              .filter(slot -> parkedCars.get(slot) != null)
                              .count();
    }

    public String getTimeOfParking(Vehicle vehicle) {
        for (Slot slot : parkedCars) {
            if (slot != null && slot.getVehicle() == vehicle)
                return slot.getCurrentDateTime();
        }
        return null;
    }

    public int getSlotOfVehicleParked(Vehicle vehicle) {
        int slotNumber = 0;
        for (Slot slot : parkedCars) {
            if (slot != null && slot.getVehicle() == vehicle) {
                slotNumber = parkedCars.indexOf(slot);
            }
        }
        return slotNumber;
    }

    public int getIndexOfSlotWithConsecutiveEmptySlot() {
        return IntStream.range(0, parkedCars.size() - 1)
                        .filter(index -> parkedCars.get(index) == null
                                         && parkedCars.get(index + 1) == null)
                        .findFirst().orElse(0);
    }

    public boolean IsAnySlotForLargeVehicleAvailable() {
        return IntStream.range(0, parkedCars.size() - 1)
                .anyMatch(index -> parkedCars.get(index) == null && parkedCars.get(index + 1) == null);
    }
}