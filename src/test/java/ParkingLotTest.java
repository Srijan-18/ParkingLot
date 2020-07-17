import org.junit.Assert;
import org.junit.Test;
import parkinglot.service.ParkingLotService;

public class ParkingLotTest {
    @Test
    public void givenCarRegistrationNumber_WhenParked_ShouldReturnTrue() {
        String carNumber = "UP12 AN3456";
        ParkingLotService parkingLotService = new ParkingLotService();
        parkingLotService.parkTheCar(carNumber);
        boolean status = parkingLotService.isCarPresent(carNumber);
        Assert.assertTrue(status);
    }
}
