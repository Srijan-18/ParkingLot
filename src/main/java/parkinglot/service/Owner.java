package parkinglot.service;

public class Owner implements IAuthority {

    @Override
    public boolean parkingSpaceAvailability(ParkingLotService parkingLotService) {
        return !parkingLotService.checkParkingLotStatus();
    }
}
