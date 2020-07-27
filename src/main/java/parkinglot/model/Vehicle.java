package parkinglot.model;

import java.util.Objects;

public class Vehicle {

    public enum DriverCategory {
        HANDICAPPED, NORMAL
    }

    public enum VehicleSize {
        LARGE, SMALL;
    }

    public enum VehicleColour {
        WHITE, BLUE, NOT_SPECIFIED;
    }

    public enum VehicleCompany {
        TOYOTA, BMW, NOT_SPECIFIED
    }

    public DriverCategory driverCategory;
    public VehicleSize vehicleSize;
    public VehicleColour vehicleColour;
    public VehicleCompany vehicleCompany;
    public String plateNumber;

    public Vehicle(DriverCategory driverCategory, VehicleSize vehicleSize, VehicleColour vehicleColour,
                   VehicleCompany vehicleCompany, String plateNumber) {
        this.driverCategory = driverCategory;
        this.vehicleSize = vehicleSize;
        this.vehicleColour = vehicleColour;
        this.vehicleCompany = vehicleCompany;
        this.plateNumber = plateNumber;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) that;
        return Objects.equals(plateNumber, vehicle.plateNumber);
    }
}