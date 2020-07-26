package parkinglot.service;


import parkinglot.model.Slot;
import parkinglot.model.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLot {
    private List<Slot> allSlots;

    public ParkingLot(int parkingLotSize) {
        allSlots = new ArrayList<>(Collections.nCopies(parkingLotSize, null));
    }

    public List<Slot> getAllSlots() {
        return allSlots;
    }

    public int getNumberOfVehiclesParked() {
        return (int) IntStream.range(0, allSlots.size())
                .filter(slot -> allSlots.get(slot) != null)
                .count();
    }

    public String getTimeOfParking(Vehicle vehicle) {
        for (Slot slot : allSlots) {
            if (slot != null && slot.getVehicle().equals(vehicle))
                return slot.getVehicleParkingDateTime();
        }
        return null;
    }

    public int getSlotOfVehicleParked(Vehicle vehicle) {
        int slotNumber = 0;
        for (Slot slot : allSlots) {
            if (slot != null && slot.getVehicle().equals(vehicle)) {
                slotNumber = allSlots.indexOf(slot);
            }
        }
        return slotNumber;
    }

    public int getIndexOfSlotWithConsecutiveEmptySlot() {
        return IntStream.range(0, allSlots.size() - 1)
                .filter(index -> allSlots.get(index) == null
                        && allSlots.get(index + 1) == null)
                .findFirst().orElse(0);
    }

    public boolean IsAnySlotForLargeVehicleAvailable() {
        return IntStream.range(0, allSlots.size() - 1)
                .anyMatch(index -> allSlots.get(index) == null && allSlots.get(index + 1) == null);
    }
}