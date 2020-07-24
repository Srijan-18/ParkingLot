package parkinglot.model;

public class Slot {
    private final Vehicle vehicle;
    private final String currentDateTime;

    public Slot(Vehicle vehicle, String currentDateTime) {
        this.vehicle = vehicle;
        this.currentDateTime = currentDateTime;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public String getCurrentDateTime() {
        return this.currentDateTime;
    }
}