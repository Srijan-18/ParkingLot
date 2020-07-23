import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parkinglot.exception.ParkingLotServiceException;
import parkinglot.observers.AirportSecurity;
import parkinglot.observers.Owner;
import parkinglot.service.ParkingLotService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ParkingLotTest {
    private ParkingLotService parkingLotService;
    private AirportSecurity airportSecurity;
    private Owner owner;

    @Before
    public void setUp() {
        parkingLotService = new ParkingLotService(3);
        airportSecurity = new AirportSecurity();
        owner = new Owner();
        parkingLotService.addObserver(owner);
        parkingLotService.addObserver(airportSecurity);
    }

    @Test
    public void givenAVehicle_WhenParked_ShouldReturnTrue() {
        Object vehicle = new Object();
        parkingLotService.parkTheVehicle(vehicle);
        boolean status = parkingLotService.isVehiclePresent(vehicle);
        Assert.assertTrue(status);
    }

    @Test
    public void givenAVehicleParked_WhenUnParked_ShouldReturnFalse() {
        Object vehicle = new Object();
        parkingLotService.parkTheVehicle(vehicle);
        parkingLotService.unParkTheVehicle(vehicle);
        boolean status = parkingLotService.isVehiclePresent(vehicle);
        Assert.assertFalse(status);
    }

    @Test
    public void givenParkingLotWithItsSize_WhenFullyOccupiedAndQueriedByOwner_ShouldReturnTrue() {
        Object[] vehicles = {new Object(), new Object(), new Object()};
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        boolean status = owner.getParkingLotStatus();
        Assert.assertTrue(status);
    }

    @Test
    public void givenVehiclesToPark_WhenAskedToParkBeyondSize_ShouldThrowAnException() {
        try {
            Object[] vehicles = {new Object(), new Object(), new Object(), new Object()};
            Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.PARKING_FULL, exception.exceptionType);
        }
    }

    @Test
    public void givenVehicleToUnPark_WhenNotPresent_ShouldThrowAnException() {
        try {
            parkingLotService.unParkTheVehicle(new Object());
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT, exception.exceptionType);
        }
    }

    @Test
    public void givenParkingLot_WhenFullAndQueriedForFullParkingLotByAirportSecurity_ShouldReturnTrue() {
        Object[] vehicles = {new Object(), new Object(), new Object()};
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        boolean status = airportSecurity.getParkingLotStatus();
        Assert.assertTrue(status);
    }

    @Test
    public void givenParkingLot_WhenParkingLotNotFullAndCheckedByOwner_ShouldReturnFalse() {
        Object[] vehicles = {new Object(), new Object()};
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        boolean status = owner.getParkingLotStatus();
        Assert.assertFalse(status);
    }

    @Test
    public void givenAParkedVehicle_WhenQueriedForSlotNumber_ShouldReturnSlotNumber() {
        Object[] vehicles = {new Object(), new Object(), new Object()};
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        Assert.assertEquals(2, parkingLotService.getSlotOfParkedVehicle(vehicles[1]));
    }

    @Test
    public void givenAVehicleNotPresentInParkingLot_WhenQueriedForSlotNumber_ShouldThrowAnException() {
        try
        {
            parkingLotService.getSlotOfParkedVehicle(new Object());
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT, exception.exceptionType );
        }
    }

    @Test
    public void givenAVehicle_WhenAlreadyParked_ShouldThrowAnException() {
        try {
            Object vehicle = new Object();
            parkingLotService.parkTheVehicle(vehicle);
            parkingLotService.parkTheVehicle(vehicle);
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_ALREADY_PARKED,
                                exception.exceptionType);
        }
    }

    @Test
    public void givenVehicleToPark_WhenParkedAndQueriedForTimeOfParking_ShouldReturnCurrentTime() {
        Object vehicle = new Object();
        parkingLotService.parkTheVehicle(vehicle);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
        LocalDateTime now = LocalDateTime.now();
        Assert.assertEquals(dtf.format(now), parkingLotService.getTimeOfParkingForVehicle(vehicle));
    }
}