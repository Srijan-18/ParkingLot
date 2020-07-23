package parkinglot.service;

public interface IAuthority {

    void fullCapacityReached(boolean status);
    boolean getParkingLotStatus();
}