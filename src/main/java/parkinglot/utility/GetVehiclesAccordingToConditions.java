package parkinglot.utility;

import parkinglot.exception.ParkingLotServiceException;
import parkinglot.model.Slot;
import parkinglot.model.Vehicle;
import parkinglot.service.ParkingLot;

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
}