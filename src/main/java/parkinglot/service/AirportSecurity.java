package parkinglot.service;

public class AirportSecurity implements IAuthority {

    @Override
    public boolean parkingSpaceAvailability(ParkingLotService parkingLotService) {
        return !parkingLotService.checkParkingLotStatus();
    }
}
