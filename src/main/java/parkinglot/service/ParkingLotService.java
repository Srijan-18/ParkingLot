package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;
import parkinglot.model.Slot;
import parkinglot.model.Vehicle;
import parkinglot.utility.ParkingLotAllotment;
import parkinglot.utility.ParkingUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotService {
    private static final int INDEX_FACTOR = 1;
    private final int numberOfLots;
    private final List<ParkingLot> parkingLots;
    private final int parkingLotSize;
    private final List<IAuthority> observerList;
    private final ParkingLotAllotment parkingLotAllotment;
    private final ParkingAttendant parkingAttendant;

    public ParkingLotService(int singleParkingLotSize, int numberOfLots) {
        this.numberOfLots = numberOfLots;
        this.parkingLots = new ArrayList<>();
        IntStream.range(0, numberOfLots)
                .forEachOrdered(index -> parkingLots.add(index, new ParkingLot(singleParkingLotSize)));
        this.parkingLotSize = singleParkingLotSize;
        observerList = new ArrayList<>();
        parkingLotAllotment = new ParkingLotAllotment(singleParkingLotSize);
        parkingAttendant = new ParkingAttendant();
    }

    public void parkTheVehicle(Vehicle vehicle) {
        if (this.checkParkingLotStatus())
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.PARKING_FULL,
                    "NO MORE SPACE TO PARK.");
        if (isVehiclePresent(vehicle))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_ALREADY_PARKED,
                    "GIVEN VEHICLE IS ALREADY PRESENT IN PARKING LOT.");
        ParkingLot parkingLotToPark = parkingLotAllotment.getTheLotToPark(parkingLots, vehicle);
        if (parkingLotToPark == null)
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.NO_SPACE_FOR_LARGE_VEHICLE,
                    "NO MORE SPACE TO PARK.");
        int slotToPark = this.getParkingSlotToPark(parkingLotToPark, vehicle);
        parkingLotToPark.getAllSlots().set(slotToPark, new Slot(vehicle, new ParkingUtility().getCurrentDateTime(),
                                        parkingAttendant.getAttendant()));
        if (checkParkingLotStatus())
            this.notifyObserversOfFullParkingLot();
    }

    public boolean isVehiclePresent(Vehicle vehicle) {
        return parkingLots.stream().findFirst()
                .filter(parkingLot -> parkingLot.getAllSlots().stream()
                        .anyMatch(slot -> slot != null && slot.getVehicle().equals(vehicle))).isPresent();
    }

    public void unParkTheVehicle(Vehicle vehicle) {
        ParkingLot lotOfVehicle = this.getLotOfParkedVehicle(vehicle);
        lotOfVehicle.getAllSlots().stream().filter(slot -> slot != null && slot.getVehicle().equals(vehicle))
                .forEach(slot -> lotOfVehicle.getAllSlots().set(lotOfVehicle.getAllSlots().indexOf(slot), null));
        if (!checkParkingLotStatus())
            this.notifyObserversOfAvailability();
    }

    private void notifyObserversOfAvailability() {
        for (IAuthority observer : observerList) {
            observer.spaceAvailableForParking();
        }
    }

    public boolean checkParkingLotStatus() {
        int numOfCarsParked = parkingLots.stream().mapToInt(ParkingLot::getNumberOfVehiclesParked).sum();
        return this.numberOfLots * this.parkingLotSize == numOfCarsParked;
    }

    public void addObserver(IAuthority authority) {
        this.observerList.add(authority);
    }

    private void notifyObserversOfFullParkingLot() {
        for (IAuthority observer : observerList) {
            observer.fullCapacityReached();
        }
    }

    private int getParkingSlotToPark(ParkingLot parkingLot, Vehicle vehicle) {
        if (vehicle.vehicleCategory.equals(Vehicle.VehicleCategory.LARGE))
            return parkingLot.getIndexOfSlotWithConsecutiveEmptySlot();
        return IntStream.range(0, parkingLot.getAllSlots().size())
                .filter(index -> parkingLot.getAllSlots().get(index) == null)
                .findFirst().orElse(-1);
    }

    public String getSlotOfParkedVehicle(Vehicle vehicle) {
        ParkingLot parkingLot = this.getLotOfParkedVehicle(vehicle);
        int slotNumberInItsLot = parkingLot.getSlotOfVehicleParked(vehicle);
        return "Parking Lot:" + (parkingLots.indexOf(parkingLot) + INDEX_FACTOR) + " Slot:" + (slotNumberInItsLot + INDEX_FACTOR);
    }

    private ParkingLot getLotOfParkedVehicle(Vehicle vehicle) {
        for (ParkingLot parkingLot : parkingLots)
            for (Slot slot : parkingLot.getAllSlots()) {
                if (slot != null && slot.getVehicle().equals(vehicle)) {
                    return parkingLot;
                }
            }
        throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT,
                "VEHICLE NOT PRESENT");
    }

    public String getTimeOfParkingForVehicle(Vehicle vehicle) {
        for (ParkingLot parkingLot : parkingLots)
            if (parkingLot.getTimeOfParking(vehicle) != null)
                return parkingLot.getTimeOfParking(vehicle);
        throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT,
                "VEHICLE NOT PRESENT IN PARKING LOT.");
    }

    public String[] getLocationOfCarsOfParticularColour(Vehicle.VehicleColour vehicleColour) {
        String[] whiteColouredCars = new String[this.parkingLotSize];
        int count = 0;
        for (ParkingLot parkingLot : parkingLots)
            for (Slot slot : parkingLot.getAllSlots()) {
                if (slot != null && slot.getVehicle().vehicleColour.equals(vehicleColour))
                    whiteColouredCars[count++] = this.getSlotOfParkedVehicle(slot.getVehicle());
            }
        if (count == 0)
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.NO_SUCH_VEHICLE_PRESENT,
                    "NO WHITE CARS");
        return whiteColouredCars;
    }

    public void addParkingAttendant(String attendantName) {
        parkingAttendant.addParkingAttendant(attendantName);
    }

    public Slot[] getDetailsOfBlueToyotaCarsParked() {
        Slot[] slotsOfBlueToyotaCars = new Slot[this.parkingLotSize];
        int count = 0;
        for (ParkingLot parkingLot : parkingLots)
            for (Slot slot : parkingLot.getAllSlots()) {
                if (slot != null && slot.getVehicle().vehicleColour.equals(Vehicle.VehicleColour.BLUE)
                        && slot.getVehicle().vehicleCompany.equals(Vehicle.VehicleCompany.TOYOTA))
                    slotsOfBlueToyotaCars[count++] = slot;
            }
        if (count == 0)
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.NO_SUCH_VEHICLE_PRESENT,
                    "NO BLUE TOYOTA CARS PARKED");
        return slotsOfBlueToyotaCars;
    }
}