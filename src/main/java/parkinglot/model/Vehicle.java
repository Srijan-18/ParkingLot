package parkinglot.model;

public class Vehicle {

    public enum DriverCategory {
        HANDICAPPED, NORMAL
    }

    public DriverCategory driverCategory;

    public Vehicle(DriverCategory driverCategory) {
        this.driverCategory = driverCategory;
    }
}