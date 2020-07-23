package parkinglot.service;

public class ParkingLotService {

    private Object vehicle;

    public ParkingLotService() {
        this.vehicle = null;
    }

    public void parkTheVehicle(Object vehicle) {
        this.vehicle = vehicle;
    }

    public boolean isVehiclePresent(Object vehicle) {
        if(this.vehicle == null)
            return false;
        return this.vehicle.equals(vehicle);
    }

    public void unParkTheVehicle() {
       this.vehicle = null;
    }
}