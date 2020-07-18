import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parkinglot.exception.ParkingLotServiceException;
import parkinglot.service.AirportSecurity;
import parkinglot.service.IAuthority;
import parkinglot.service.Owner;
import parkinglot.service.ParkingLotService;

public class ParkingLotTest {
    private ParkingLotService parkingLotService;
    @Before
    public void setUp()  {
        parkingLotService = new ParkingLotService();
    }

    @Test
    public void givenCarRegistrationNumber_WhenParked_ShouldReturnTrue() {
        String carNumber = "UP12 AN3456";
        parkingLotService.parkTheCar(carNumber);
        boolean status = parkingLotService.isCarPresent(carNumber);
        Assert.assertTrue(status);
    }

    @Test
    public void givenCarRegistrationNumber_WhenUnParked_ShouldReturnFalse() {
        String carNumber1 = "UP12 AN3456";
        String carNumber2 = "UP34 AN5678";
        parkingLotService.parkTheCar(carNumber1);
        parkingLotService.parkTheCar(carNumber2);
        parkingLotService.unParkTheCar(carNumber1);
        boolean status = parkingLotService.isCarPresent(carNumber1);
        Assert.assertFalse(status);
    }

    @Test
    public void givenParkingLot_WhenFullAndQueriedForParkingAvailabilityByOwner_ShouldReturnFalse() {
        int size = 3;
        parkingLotService.setParkingLotSize(size);
        String[] carNumber = {"UP12 AN3456", "UP34 AN5678", "UP56 QW1234"};
        for (String car:carNumber)
            parkingLotService.parkTheCar(car);
        IAuthority owner = new Owner();
        boolean status = owner.parkingSpaceAvailability(parkingLotService);
        Assert.assertTrue(status);
    }

    @Test
    public void givenCarsToPark_WhenAskedToParkBeyondSize_ShouldThrowAnException() {
        try {
            int size = 3;
            parkingLotService.setParkingLotSize(size);
            String[] carNumber = {"UP12 AN3456", "UP34 AN5678", "UP56 QW1234", "UP31 AS7894"};
            for (String car:carNumber)
                parkingLotService.parkTheCar(car);
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.PARKING_FULL, exception.exceptionType);
        }
    }

    @Test
    public void givenCarToUnPark_WhenCarNotPresent_ShouldThrowAnException() {
        try {
            int size = 3;
            parkingLotService.setParkingLotSize(size);
            parkingLotService.unParkTheCar("UP12 AB3456");
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.CAR_NOT_PRESENT, exception.exceptionType);
        }
    }

    @Test
    public void givenParkingLot_WhenFullAndQueriedForParkingAvailabilityByAirportSecurity_ShouldReturnFalse() {
        int size = 4;
        parkingLotService.setParkingLotSize(size);
        String[] carNumber = {"UP12 AN3456", "UP34 AN5678", "UP56 QW1234", "UP31 AS7894"};
        for (String car:carNumber)
            parkingLotService.parkTheCar(car);
        IAuthority airportSecurity = new AirportSecurity();
        boolean status = airportSecurity.parkingSpaceAvailability(parkingLotService);
        Assert.assertFalse(status);
    }
}