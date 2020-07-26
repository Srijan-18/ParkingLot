package parkinglot.model;

public class Slot {
    private final Vehicle vehicle;
    private final String vehicleParkingDateTime;
    private final String attendantName;

    public Slot(Vehicle vehicle, String vehicleParkingDateTime, String attendantName) {
        this.vehicle = vehicle;
        this.vehicleParkingDateTime = vehicleParkingDateTime;
        this.attendantName = attendantName;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public String getVehicleParkingDateTime() {

        return this.vehicleParkingDateTime;
    }

    public String getAttendantName() {

        return this.attendantName;
    }
}