import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parkinglot.service.ParkingLotService;

public class ParkingLotTest {
    private ParkingLotService parkingLotService;
    @Before
    public void setUp() {
        parkingLotService = new ParkingLotService();
    }

    @Test
    public void givenAVehicle_WhenParked_ShouldReturnTrue() {
        Object vehicle = new Object();
        ParkingLotService parkingLotService = new ParkingLotService();
        parkingLotService.parkTheVehicle(vehicle);
        boolean status = parkingLotService.isVehiclePresent(vehicle);
        Assert.assertTrue(status);
    }

    @Test
    public void givenAVehicleParked_WhenUnParked_ShouldReturnFalse() {
        Object vehicle = new Object();
        parkingLotService.parkTheVehicle(vehicle);
        parkingLotService.unParkTheVehicle();
        boolean status = parkingLotService.isVehiclePresent(vehicle);
        Assert.assertFalse(status);
    }
}