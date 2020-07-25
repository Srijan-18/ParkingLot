package parkinglot.model;

public class Vehicle {

    public enum DriverCategory {
        HANDICAPPED, NORMAL
    }

    public enum VehicleCategory {
        LARGE, NORMAL;
    }

    public enum VehicleColour {
        WHITE, NOT_SPECIFIED;
    }

    public DriverCategory driverCategory;
    public VehicleCategory vehicleCategory;
    public VehicleColour vehicleColour;

    public Vehicle(DriverCategory driverCategory, VehicleCategory vehicleCategory, VehicleColour vehicleColour) {
        this.driverCategory = driverCategory;
        this.vehicleCategory = vehicleCategory;
        this.vehicleColour = vehicleColour;
    }
}