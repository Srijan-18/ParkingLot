package parkinglot.model;

public class Vehicle {

    public enum DriverCategory {
        HANDICAPPED, NORMAL
    }

    public enum VehicleCategory {
        LARGE, NORMAL;
    }

    public DriverCategory driverCategory;
    public VehicleCategory vehicleCategory;

    public Vehicle(DriverCategory driverCategory, VehicleCategory vehicleCategory) {
        this.driverCategory = driverCategory;
        this.vehicleCategory = vehicleCategory;
    }
}