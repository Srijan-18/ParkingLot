package parkinglot.utility;

import parkinglot.exception.ParkingLotServiceException;
import parkinglot.model.Slot;
import parkinglot.model.Vehicle;
import parkinglot.service.ParkingLot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GetVehiclesAccordingToConditions {

    public List<Slot> getDetailsOfVehicleAccordingToCompany(List<ParkingLot> parkingLots,
                                                            Vehicle.VehicleCompany searchVehicleCompany) {
        List<Slot> givenCompanyVehicles = new ArrayList<>();
        for (ParkingLot parkingLot : parkingLots)
            givenCompanyVehicles.addAll(IntStream.range(0, parkingLot.getAllSlots().size())
                    .filter(index -> parkingLot.getAllSlots().get(index) != null
                            && parkingLot.getAllSlots().get(index).getVehicle().vehicleCompany
                            .equals(searchVehicleCompany))
                    .mapToObj(parkingLot.getAllSlots()::get).collect(Collectors.toList()));
        if (givenCompanyVehicles.size() == 0)
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.NO_SUCH_VEHICLE_PRESENT,
                    "NO SUCH VEHICLE PRESENT");
        return givenCompanyVehicles;
    }

    public List<Slot> getDetailsOfGivenColouredVehicles(List<ParkingLot> parkingLots,
                                                        Vehicle.VehicleColour searchVehicleColour) {
        List<Slot> givenCompanyVehicles = new ArrayList<>();
        for (ParkingLot parkingLot : parkingLots)
            givenCompanyVehicles.addAll(IntStream.range(0, parkingLot.getAllSlots().size())
                    .filter(index -> parkingLot.getAllSlots().get(index) != null
                            && parkingLot.getAllSlots().get(index).getVehicle().vehicleColour
                            .equals(searchVehicleColour))
                    .mapToObj(parkingLot.getAllSlots()::get).collect(Collectors.toList()));
        if (givenCompanyVehicles.size() == 0)
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.NO_SUCH_VEHICLE_PRESENT,
                    "NO SUCH VEHICLE PRESENT");
        return givenCompanyVehicles;
    }

    public List<Slot> getVehiclesInGivenTimeRangeInMinutes(List<ParkingLot> parkingLots, int searchTimeRange) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime currentDateTime = LocalDateTime.parse(new ParkingUtility().getCurrentDateTime(),
                dateTimeFormatter);
        List<Slot> vehiclesParkedInGivenTimeRange = new ArrayList<>();
        for (ParkingLot parkingLot : parkingLots)
            vehiclesParkedInGivenTimeRange.addAll(IntStream.range(0, parkingLot.getAllSlots().size())
                    .filter(index -> parkingLot.getAllSlots().get(index) != null
                            && ChronoUnit.MINUTES.between(currentDateTime,
                            LocalDateTime.parse(parkingLot.getAllSlots().get(index).getVehicleParkingDateTime(),
                                    dateTimeFormatter)) <= searchTimeRange)
                    .mapToObj(parkingLot.getAllSlots()::get).collect(Collectors.toList()));
        if (vehiclesParkedInGivenTimeRange.size() == 0)
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.NO_SUCH_VEHICLE_PRESENT,
                    "NO VEHICLE PARKED IN GIVEN TIME RANGE");
        return vehiclesParkedInGivenTimeRange;
    }

    public List<Slot> getVehiclesWithGivenSizeAndDriverCategoryAndInGivenLots(List<ParkingLot> parkingLots,
                                                                              Vehicle.DriverCategory searchDriverCategory,
                                                                              Vehicle.VehicleSize searchVehicleSize,
                                                                              int[] lots) {
        List<ParkingLot> lotsToSearch = new ArrayList<>();
        IntStream.range(0, lots.length).forEachOrdered(index -> lotsToSearch.add(index,
                parkingLots.get(lots[index] - 1)));
        List<Slot> detailsOfVehicles = new ArrayList<>();
        for (ParkingLot parkingLot : lotsToSearch)
            detailsOfVehicles.addAll(IntStream.range(0, parkingLot.getAllSlots().size())
                    .filter(index -> parkingLot.getAllSlots().get(index) != null
                            && parkingLot.getAllSlots().get(index).getVehicle().vehicleSize
                            .equals(searchVehicleSize)
                            && parkingLot.getAllSlots().get(index).getVehicle().driverCategory
                            .equals(searchDriverCategory))
                    .mapToObj(parkingLot.getAllSlots()::get).collect(Collectors.toList()));
        if (detailsOfVehicles.size() == 0)
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.NO_SUCH_VEHICLE_PRESENT,
                    "NO SUCH VEHICLE PRESENT");
        return detailsOfVehicles;
    }

    public List<Vehicle> getAllVehiclesParked(List<ParkingLot> parkingLots) {
        List<Slot> allOccupiedSlots = new ArrayList<>();
        for (ParkingLot parkingLot : parkingLots)
            allOccupiedSlots.addAll(IntStream.range(0, parkingLot.getAllSlots().size())
                    .filter(index -> parkingLot.getAllSlots().get(index) != null)
                    .mapToObj(parkingLot.getAllSlots()::get).collect(Collectors.toList()));
        if (allOccupiedSlots.size() == 0)
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.EMPTY_PARKING_LOT,
                    "NO VEHICLES IN PARKING LOT");
        List<Vehicle> allVehiclesPresent = new ArrayList<>();
        IntStream.range(0, allOccupiedSlots.size())
                .forEachOrdered(index -> allVehiclesPresent.add(allOccupiedSlots.get(index).getVehicle()));
        return allVehiclesPresent;
    }
}