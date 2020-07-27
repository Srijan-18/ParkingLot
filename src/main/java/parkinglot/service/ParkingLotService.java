package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;
import parkinglot.model.Slot;
import parkinglot.model.Vehicle;
import parkinglot.utility.GetVehiclesAccordingToConditions;
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
    private final GetVehiclesAccordingToConditions getVehiclesAccordingToConditions;

    public ParkingLotService(int singleParkingLotSize, int numberOfLots) {
        this.numberOfLots = numberOfLots;
        this.parkingLots = new ArrayList<>();
        IntStream.range(0, numberOfLots)
                .forEachOrdered(index -> parkingLots.add(index, new ParkingLot(singleParkingLotSize)));
        this.parkingLotSize = singleParkingLotSize;
        observerList = new ArrayList<>();
        parkingLotAllotment = new ParkingLotAllotment(singleParkingLotSize);
        parkingAttendant = new ParkingAttendant();
        getVehiclesAccordingToConditions = new GetVehiclesAccordingToConditions();
    }

    public void parkTheVehicle(Vehicle vehicle) {
        if (this.checkParkingLotStatus())
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.PARKING_FULL,
                    "NO MORE SPACE TO PARK.");
        if (isVehiclePresent(vehicle))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_ALREADY_PARKED,
                    "GIVEN VEHICLE IS ALREADY PRESENT IN PARKING LOT.");
        ParkingLot parkingLotToPark = parkingLotAllotment.getTheLotToPark(parkingLots, vehicle);
        int slotToPark = this.getParkingSlotToPark(parkingLotToPark, vehicle);
        parkingLotToPark.getAllSlots().set(slotToPark, new Slot(vehicle, new ParkingUtility().getCurrentDateTime(),
                parkingAttendant.getAttendant()));
        if (checkParkingLotStatus())
            observerList.forEach(IAuthority::fullCapacityReached);
    }

    public boolean isVehiclePresent(Vehicle vehicle) {
        return parkingLots.stream().findFirst()
                .filter(parkingLot -> parkingLot.getAllSlots().stream()
                        .anyMatch(slot -> slot != null && slot.getVehicle().equals(vehicle))).isPresent();
    }

    public void unParkTheVehicle(Vehicle vehicle) {
        ParkingLot lotOfVehicle = parkingLotAllotment.getLotOfParkedVehicle(parkingLots, vehicle);
        lotOfVehicle.getAllSlots().stream().filter(slot -> slot != null && slot.getVehicle().equals(vehicle))
                .forEach(slot -> lotOfVehicle.getAllSlots().set(lotOfVehicle.getAllSlots().indexOf(slot), null));
        observerList.forEach(IAuthority::spaceAvailableForParking);
    }

    public void addObserver(IAuthority authority) {
        this.observerList.add(authority);
    }

    public boolean checkParkingLotStatus() {
        int numOfCarsParked = parkingLots.stream().mapToInt(ParkingLot::getNumberOfVehiclesParked).sum();
        return this.numberOfLots * this.parkingLotSize == numOfCarsParked;
    }

    private int getParkingSlotToPark(ParkingLot parkingLot, Vehicle vehicle) {
        if (vehicle.vehicleSize.equals(Vehicle.VehicleSize.LARGE))
            return parkingLot.getIndexOfSlotWithConsecutiveEmptySlot();
        return IntStream.range(0, parkingLot.getAllSlots().size())
                .filter(index -> parkingLot.getAllSlots().get(index) == null)
                .findFirst().orElse(-1);
    }

    public String getSlotOfParkedVehicle(Vehicle vehicle) {
        ParkingLot parkingLot = parkingLotAllotment.getLotOfParkedVehicle(parkingLots, vehicle);
        int slotNumberInItsLot = parkingLot.getSlotOfVehicleParked(vehicle);
        return "Parking Lot:" + (parkingLots.indexOf(parkingLot) + INDEX_FACTOR) +
                " Slot:" + (slotNumberInItsLot + INDEX_FACTOR);
    }

    public String getTimeOfParkingForVehicle(Vehicle vehicle) {
        for (ParkingLot parkingLot : parkingLots)
            if (parkingLot.getTimeOfParking(vehicle) != null)
                return parkingLot.getTimeOfParking(vehicle);
        throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT,
                "VEHICLE NOT PRESENT IN PARKING LOT.");
    }

    public String[] getLocationOfVehiclesOfParticularColour(Vehicle.VehicleColour vehicleColour) {
        List<Slot> givenColouredVehicles = getVehiclesAccordingToConditions
                .getDetailsOfGivenColouredVehicles(parkingLots, vehicleColour);
        String[] givenColouredVehicleSlotNumbers = new String[givenColouredVehicles.size()];
        IntStream.range(0, givenColouredVehicles.size() - 1)
                .forEachOrdered(index -> givenColouredVehicleSlotNumbers[index] = this.getSlotOfParkedVehicle
                        (givenColouredVehicles.get(index).getVehicle()));
        return givenColouredVehicleSlotNumbers;
    }

    public void addParkingAttendant(String attendantName) {
        parkingAttendant.addParkingAttendant(attendantName);
    }

    public List<Slot> getDetailsOfBlueToyotaVehiclesParked() {
        List<Slot> givenColouredVehicles = getVehiclesAccordingToConditions
                .getDetailsOfGivenColouredVehicles(parkingLots, Vehicle.VehicleColour.BLUE);
        givenColouredVehicles.retainAll(getVehiclesAccordingToConditions
                .getDetailsOfVehicleAccordingToCompany(parkingLots, Vehicle.VehicleCompany.TOYOTA));
        return givenColouredVehicles;
    }

    public List<Slot> getDetailsOfBMWVehicles() {
        return getVehiclesAccordingToConditions.getDetailsOfVehicleAccordingToCompany
                (parkingLots, Vehicle.VehicleCompany.BMW);
    }

    public List<Slot> getDetailsOfVehiclesParkedInLast30Minutes() {
        return getVehiclesAccordingToConditions.getVehiclesInGivenTimeRangeInMinutes(parkingLots, 30);
    }

    public List<Slot> getDetailsOfSmallVehiclesWithHandicappedDriversInLot2and4() {
        return getVehiclesAccordingToConditions
                .getVehiclesWithGivenSizeAndDriverCategoryAndInGivenLots(parkingLots, Vehicle.DriverCategory.HANDICAPPED,
                                                                         Vehicle.VehicleSize.SMALL, new int[]{2, 4});
    }

    public List<Vehicle> getAllParkedVehicles() {
        return getVehiclesAccordingToConditions.getAllVehiclesParked(parkingLots);
    }
}