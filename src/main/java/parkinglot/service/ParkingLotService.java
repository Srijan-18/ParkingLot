package parkinglot.service;

public class ParkingLotService {

    private Object vehicle;

    public void parkTheCar(Object vehicle) {
        this.vehicle = vehicle;
    }

    public boolean isCarPresent(Object vehicle) {
        return this.vehicle.equals(vehicle);
    }
}
