package parkinglot.service;

public interface IAuthority {

    void fullCapacityReached();

    boolean getParkingLotStatus();

    void spaceAvailableForParking();
}