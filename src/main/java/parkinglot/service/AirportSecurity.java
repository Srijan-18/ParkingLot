package parkinglot.service;

public class AirportSecurity {

    public boolean parkingSpaceAvailability(ParkingLotService parkingLotService) {
        return !parkingLotService.checkParkingLotStatus();
    }
}
