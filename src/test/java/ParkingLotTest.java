import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parkinglot.exception.ParkingLotServiceException;
import parkinglot.model.Vehicle;
import parkinglot.observers.AirportSecurity;
import parkinglot.observers.Owner;
import parkinglot.service.ParkingLotService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ParkingLotTest {
    private ParkingLotService parkingLotService;
    private AirportSecurity airportSecurity;
    private Owner owner;

    @Before
    public void setUp() {
        parkingLotService = new ParkingLotService(3, 1);
        airportSecurity = new AirportSecurity();
        owner = new Owner();
        parkingLotService.addObserver(owner);
        parkingLotService.addObserver(airportSecurity);
    }

    @Test
    public void givenAVehicle_WhenParked_ShouldReturnTrue() {
        Vehicle vehicle = new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL);
        parkingLotService.parkTheVehicle(vehicle);
        boolean status = parkingLotService.isVehiclePresent(vehicle);
        Assert.assertTrue(status);
    }

    @Test
    public void givenAVehicleParked_WhenUnParked_ShouldReturnFalse() {
        Vehicle vehicle = new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL);
        parkingLotService.parkTheVehicle(vehicle);
        parkingLotService.unParkTheVehicle(vehicle);
        boolean status = parkingLotService.isVehiclePresent(vehicle);
        Assert.assertFalse(status);
    }

    @Test
    public void givenParkingLotWithItsSize_WhenFullyOccupiedAndQueriedByOwner_ShouldReturnTrue() {
        Vehicle[] vehicles = {new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL)};
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        boolean status = owner.getParkingLotStatus();
        Assert.assertTrue(status);
    }

    @Test
    public void givenVehiclesToPark_WhenAskedToParkBeyondSize_ShouldThrowAnException() {
        try {
            Vehicle[] vehicles = {new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                    new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                    new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                    new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL)};
            Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.PARKING_FULL, exception.exceptionType);
        }
    }

    @Test
    public void givenVehicleToUnPark_WhenNotPresent_ShouldThrowAnException() {
        try {
            parkingLotService.unParkTheVehicle(new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL));
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT, exception.exceptionType);
        }
    }

    @Test
    public void givenParkingLot_WhenFullAndQueriedForFullParkingLotByAirportSecurity_ShouldReturnTrue() {
        Vehicle[] vehicles = {new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL)};
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        boolean status = airportSecurity.getParkingLotStatus();
        Assert.assertTrue(status);
    }

    @Test
    public void givenParkingLot_WhenParkingLotNotFullAndCheckedByOwner_ShouldReturnFalse() {
        Vehicle[] vehicles = {new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL)};
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        boolean status = owner.getParkingLotStatus();
        Assert.assertFalse(status);
    }

    @Test
    public void givenAParkedVehicle_WhenQueriedForSlotNumber_ShouldReturnSlotNumber() {
        Vehicle[] vehicles = {new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL)};
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        Assert.assertEquals("P:1 S:2", parkingLotService.getSlotOfParkedVehicle(vehicles[1]));
    }

    @Test
    public void givenAVehicleNotPresentInParkingLot_WhenQueriedForSlotNumber_ShouldThrowAnException() {
        try {
            parkingLotService.getSlotOfParkedVehicle(new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL));
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT, exception.exceptionType);
        }
    }

    @Test
    public void givenAVehicle_WhenAlreadyParked_ShouldThrowAnException() {
        try {
            Vehicle vehicle = new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL);
            parkingLotService.parkTheVehicle(vehicle);
            parkingLotService.parkTheVehicle(vehicle);
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_ALREADY_PARKED,
                    exception.exceptionType);
        }
    }

    @Test
    public void givenVehicleToPark_WhenParkedAndQueriedForTimeOfParking_ShouldReturnCurrentTime() {
        Vehicle vehicle = new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL);
        parkingLotService.parkTheVehicle(vehicle);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
        LocalDateTime now = LocalDateTime.now();
        Assert.assertEquals(dateTimeFormatter.format(now), parkingLotService.getTimeOfParkingForVehicle(vehicle));
    }

    @Test
    public void givenVehicleNotPresentInParkingLot_WhenQueriedForTime_ShouldThrowAnException() {
        try {
            parkingLotService.getTimeOfParkingForVehicle(new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL));
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT, exception.exceptionType);
        }
    }

    @Test
    public void givenMultipleParkingLotsAndVehicles_WhenParked_ShouldDistributeThemEvenly() {
        ParkingLotService lotService = new ParkingLotService(3, 3);
        Vehicle[] vehicles = {new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL)};
        Arrays.stream(vehicles).forEachOrdered(lotService::parkTheVehicle);
        Assert.assertEquals("P:1 S:2", lotService.getSlotOfParkedVehicle(vehicles[3]));
    }

    @Test
    public void givenAVehicle_WhenAlreadyParkedInAnyOfLots_ShouldThrowAnException() {
        try {
            ParkingLotService parkingLotService = new ParkingLotService(3, 3);
            Vehicle[] vehicles = {new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                    new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                    new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                    new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL)};
            Arrays.stream(vehicles).forEachOrdered(parkingLotService::parkTheVehicle);
            parkingLotService.parkTheVehicle(vehicles[1]);
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_ALREADY_PARKED,
                    exception.exceptionType);
        }
    }

    @Test
    public void givenVehiclesToParkInMultipleLots_WhenExceedSize_ShouldThrowAnException() {
        try {
            ParkingLotService parkingLotService = new ParkingLotService(2, 2);
            Vehicle[] vehicles = {new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                    new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                    new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                    new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                    new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL)};
            Arrays.stream(vehicles).forEachOrdered(parkingLotService::parkTheVehicle);
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.PARKING_FULL, exception.exceptionType);
        }
    }

    @Test
    public void givenHandicappedDriver_WhenToPark_ShouldBeGivenNearestSlot() {
        ParkingLotService parkingLotService = new ParkingLotService(3, 3);
        Vehicle[] vehicles = {new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.HANDICAPPED, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL)};
        Arrays.stream(vehicles).forEachOrdered(parkingLotService::parkTheVehicle);
        Assert.assertEquals("P:1 S:2", parkingLotService.getSlotOfParkedVehicle(vehicles[2]));
    }

    @Test
    public void givenOnlyHandicappedDrivers_WhenToPark_ShouldParkSerially() {
        ParkingLotService parkingLotService = new ParkingLotService(2, 3);
        Vehicle[] vehicles = {new Vehicle(Vehicle.DriverCategory.HANDICAPPED, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.HANDICAPPED, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.HANDICAPPED, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.HANDICAPPED, Vehicle.VehicleCategory.NORMAL),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleCategory.NORMAL)};
        Arrays.stream(vehicles).forEachOrdered(parkingLotService::parkTheVehicle);
        Assert.assertEquals("P:3 S:1", parkingLotService.getSlotOfParkedVehicle(vehicles[4]));
    }

    @Test
    public void givenMultipleLargeVehiclesToPark_WhenNoTwoConsecutiveSlotsAvailable_ShouldThrowAnException() {
        ParkingLotService parkingLotService = new ParkingLotService(2, 2);
        Vehicle[] vehicles = new Vehicle[3];
        IntStream.range(0, 3).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                                                                                     Vehicle.VehicleCategory.LARGE));
        try {
            IntStream.range(0, 3).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.NO_SPACE_FOR_LARGE_VEHICLE,
                                exception.exceptionType);
        }
    }

    @Test
    public void givenLargeVehicleWithNormalDriver_WhenToPark_ShouldParkInEmptiestLotHavingConsecutiveEmptySlots() {
        ParkingLotService parkingLotService = new ParkingLotService(4, 3);
        Vehicle[] vehicles = new Vehicle[12];
        IntStream.range(0, 12).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleCategory.NORMAL ));
        IntStream.range(0, 12).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        IntStream.rangeClosed(3, 5).forEachOrdered(index -> parkingLotService.unParkTheVehicle(vehicles[index]));
        IntStream.rangeClosed(7, 9).forEachOrdered(index -> parkingLotService.unParkTheVehicle(vehicles[index]));
        parkingLotService.unParkTheVehicle(vehicles[11]);
        Vehicle largeVehicleWithNormalDriver = new Vehicle(Vehicle.DriverCategory.NORMAL,
                                                                Vehicle.VehicleCategory.LARGE);
        parkingLotService.parkTheVehicle(largeVehicleWithNormalDriver);
        Assert.assertEquals("P:3 S:2", parkingLotService
                .getSlotOfParkedVehicle(largeVehicleWithNormalDriver));
    }

    @Test
    public void givenLargeVehicleWithHandicappedDriver_WhenToPark_ShouldParkInNearestLotHavingConsecutiveEmptySlots() {
        ParkingLotService parkingLotService = new ParkingLotService(4, 3);
        Vehicle[] vehicles = new Vehicle[12];
        IntStream.range(0, 12).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                                                                                    Vehicle.VehicleCategory.NORMAL ));
        IntStream.range(0, 12).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        IntStream.rangeClosed(3, 5).forEachOrdered(index -> parkingLotService.unParkTheVehicle(vehicles[index]));
        IntStream.rangeClosed(7, 9).forEachOrdered(index -> parkingLotService.unParkTheVehicle(vehicles[index]));
        parkingLotService.unParkTheVehicle(vehicles[11]);
        Vehicle largeVehicleWithHandicappedDriver = new Vehicle(Vehicle.DriverCategory.HANDICAPPED,
                                                                Vehicle.VehicleCategory.LARGE);
        parkingLotService.parkTheVehicle(largeVehicleWithHandicappedDriver);
        Assert.assertEquals("P:2 S:2", parkingLotService
                                                .getSlotOfParkedVehicle(largeVehicleWithHandicappedDriver));
    }
}