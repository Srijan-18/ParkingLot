package parkinglot.model;

public class Slot {
    private Object vehicle;
    private String currentDateTime;

    public Slot(Object vehicle, String currentDateTime) {
        this.vehicle = vehicle;
        this.currentDateTime = currentDateTime;
    }

    public Object getVehicle() {
        return vehicle;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }
}