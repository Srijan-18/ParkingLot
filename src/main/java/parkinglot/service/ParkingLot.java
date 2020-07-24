package parkinglot.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLot {
    public List<Slot> parkedCars;
    private int parkingLotSize;


    public ParkingLot(int parkingLotSize) {
        this.parkingLotSize = parkingLotSize;
        parkedCars = new ArrayList<>(Collections.nCopies(parkingLotSize, null));
    }

    public int getNumberOfVehiclesParked() {
        return IntStream.range(0, this.parkingLotSize)
                .filter(slot -> parkedCars.get(slot) == null)
                .findFirst().orElse(this.parkingLotSize);
    }


    public String getTimeOfParking(Object vehicle) {
        for (Slot slot : parkedCars) {
            if (slot != null && slot.getVehicle() == vehicle)
                return slot.getCurrentDateTime();
        }
        return null;
    }

    public int getSlotOfVehicleParked(Object vehicle) {
        int slotNumber = 0;
        for (Slot slot : parkedCars) {
            if (slot != null && slot.getVehicle() == vehicle) {
                slotNumber = parkedCars.indexOf(slot);
            }
        }
        return slotNumber;
    }
}