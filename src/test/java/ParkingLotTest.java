import org.junit.Assert;
import org.junit.Test;
import parkinglot.service.ParkingLotService;

public class ParkingLotTest {
    @Test
    public void givenCarRegistrationNumber_WhenParked_ShouldReturnTrue() {
        Object vehicle = new Object();
        ParkingLotService parkingLotService = new ParkingLotService();
        parkingLotService.parkTheCar(vehicle);
        boolean status = parkingLotService.isCarPresent(vehicle);
        Assert.assertTrue(status);
    }
}