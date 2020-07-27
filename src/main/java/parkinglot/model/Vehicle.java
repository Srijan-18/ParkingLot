package parkinglot.model;

import java.util.Objects;

public class Vehicle {

    public enum DriverCategory {
        HANDICAPPED, NORMAL
    }

    public enum VehicleCategory {
        LARGE, NORMAL;
    }

    public enum VehicleColour {
        WHITE, BLUE, NOT_SPECIFIED;
    }

    public enum VehicleCompany {
        TOYOTA, BMW, NOT_SPECIFIED
    }

    public DriverCategory driverCategory;
    public VehicleCategory vehicleCategory;
    public VehicleColour vehicleColour;
    public VehicleCompany vehicleCompany;
    public String plateNumber;

    public Vehicle(DriverCategory driverCategory, VehicleCategory vehicleCategory, VehicleColour vehicleColour,
                   VehicleCompany vehicleCompany, String plateNumber) {
        this.driverCategory = driverCategory;
        this.vehicleCategory = vehicleCategory;
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