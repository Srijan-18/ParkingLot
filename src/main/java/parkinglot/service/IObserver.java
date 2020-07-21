package parkinglot.service;

public interface IObserver {

    void setParkingLotStatus (boolean isParkingLotFull);
    boolean isParkingLotFull();
}