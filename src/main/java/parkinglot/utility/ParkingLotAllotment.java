package parkinglot.utility;

import parkinglot.model.Vehicle;
import parkinglot.service.ParkingLot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLotAllotment {
    private final int parkingLotSize;

    public ParkingLotAllotment(int parkingLotSize) {
        this.parkingLotSize = parkingLotSize;
    }

    public ParkingLot getTheLotToPark(List<ParkingLot> parkingLots, Vehicle vehicle) {
        ParkingLot availableParkingLot = null;
        switch (vehicle.vehicleCategory) {
            case LARGE:
                availableParkingLot = getLotForLargeVehicle(vehicle, parkingLots);
                break;
            case NORMAL:
                if (vehicle.driverCategory.equals(Vehicle.DriverCategory.HANDICAPPED))
                    for (ParkingLot parkingLot : parkingLots) {
                        if (parkingLot.getNumberOfVehiclesParked() < this.parkingLotSize)
                            return parkingLot;
                    }
                List<ParkingLot> temporaryParkingLotsList = new ArrayList<>(parkingLots);
                temporaryParkingLotsList.sort(Comparator.comparing(ParkingLot::getNumberOfVehiclesParked));
                availableParkingLot = temporaryParkingLotsList.get(0);
        }
        return availableParkingLot;
    }

    private ParkingLot getLotForLargeVehicle(Vehicle vehicle, List<ParkingLot> parkingLots) {
        ParkingLot availableParkingLot = null;
        if (vehicle.driverCategory.equals(Vehicle.DriverCategory.HANDICAPPED))
            for (ParkingLot parkingLot : parkingLots) {
                if (parkingLot.getNumberOfVehiclesParked() < this.parkingLotSize
                        && parkingLot.IsAnySlotForLargeVehicleAvailable())
                    return parkingLot;
            }
        List<ParkingLot> temporaryList = IntStream.range(0, parkingLots.size())
                .filter(index -> parkingLots.get(index).IsAnySlotForLargeVehicleAvailable())
                .mapToObj(parkingLots::get).collect(Collectors.toList());
        if (temporaryList.size() > 0) {
            temporaryList.sort(Comparator.comparing(ParkingLot::getNumberOfVehiclesParked));
            availableParkingLot = temporaryList.get(0);
        }
        return availableParkingLot;
    }
}