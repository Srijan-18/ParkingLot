import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parkinglot.exception.ParkingLotServiceException;
import parkinglot.service.ParkingLotService;

public class ParkingLotTest {
    private ParkingLotService parkingLotService;

    @Before
    public void setUp() {
        parkingLotService = new ParkingLotService(3);
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
    public void givenParkingLotWithItsSize_WhenFullyOccupied_ShouldReturnTrue() {
        Object[] vehicle = {new Object(), new Object(), new Object()};
        parkingLotService.parkTheVehicle(vehicle[0]);
        parkingLotService.parkTheVehicle(vehicle[1]);
        parkingLotService.parkTheVehicle(vehicle[2]);
        boolean status = parkingLotService.checkParkingLotStatus();
        Assert.assertTrue(status);
    }

    @Test
    public void givenVehiclesToPark_WhenAskedToParkBeyondSize_ShouldThrowAnException() {
        try {
            Object[] vehicle = {new Object(), new Object(), new Object(), new Object()};
            parkingLotService.parkTheVehicle(vehicle[0]);
            parkingLotService.parkTheVehicle(vehicle[1]);
            parkingLotService.parkTheVehicle(vehicle[2]);
            parkingLotService.parkTheVehicle(vehicle[3]);
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.PARKING_FULL, exception.exceptionType);
        }
    }

    @Test
    public void givenVehicleToUnPark_WhenNotPresent_ShouldThrowAnException() {
        try {
            parkingLotService.unParkTheVehicle(new Object());
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.CAR_NOT_PRESENT, exception.exceptionType);
        }
    }
}