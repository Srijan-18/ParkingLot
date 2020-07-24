package parkinglot.observers;

import parkinglot.service.IAuthority;

public class AirportSecurity implements IAuthority {
    private boolean capacityStatus;

    @Override
    public void fullCapacityReached() {
        this.capacityStatus = true;
    }

    @Override
    public boolean getParkingLotStatus() {
        return capacityStatus;
    }

    @Override
    public void spaceAvailableForParking() {
        this.capacityStatus = false;
    }
}