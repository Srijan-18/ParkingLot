package parkinglot.observer;

import parkinglot.service.IObserver;

public class AirportSecurity implements IObserver {
    private boolean isParkingLotFull;

    @Override
    public void setParkingLotStatus(boolean isParkingLotFull) {
        this.isParkingLotFull = isParkingLotFull;
    }

    @Override
    public boolean isParkingLotFull() {
        return isParkingLotFull;
    }
}
