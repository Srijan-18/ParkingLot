package parkinglot.observers;

import parkinglot.service.IAuthority;

public class Owner implements IAuthority {
    private boolean capacityStatus;

    @Override
    public void fullCapacityReached(boolean status) {
        this.capacityStatus = status;
    }

    @Override
    public boolean getParkingLotStatus() {
        return capacityStatus;
    }
}