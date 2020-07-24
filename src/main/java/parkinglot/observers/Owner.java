package parkinglot.observers;

import parkinglot.service.IAuthority;

public class Owner implements IAuthority {
    private boolean parkingLotFull;

    @Override
    public void fullCapacityReached() {
        this.parkingLotFull = true;
    }

    @Override
    public boolean getParkingLotStatus() {
        return parkingLotFull;
    }

    @Override
    public void spaceAvailableForParking() {
        parkingLotFull = false ;
    }
}