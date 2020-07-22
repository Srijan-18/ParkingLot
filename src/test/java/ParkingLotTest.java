import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parkinglot.exception.ParkingLotServiceException;
import parkinglot.observer.AirportSecurity;
import parkinglot.observer.Owner;
import parkinglot.service.ParkingLotService;

import java.util.stream.IntStream;

public class ParkingLotTest {
    private ParkingLotService parkingLotService;
    private AirportSecurity airportSecurity;
    private Owner owner;

    @Before
    public void setUp() {
        parkingLotService = new ParkingLotService(5);
        airportSecurity = new AirportSecurity();
        owner = new Owner();
    }

    @Test
    public void givenCarRegistrationNumber_WhenParked_ShouldReturnTrue() {
        String carNumber = "UP12 AN3456";
        parkingLotService.parkTheCar(carNumber);
        boolean status = parkingLotService.isCarPresent(carNumber);
        Assert.assertTrue(status);
    }

    @Test
    public void givenCarRegistrationNumberToPark_WhenAlreadyParked_ShouldThrowException() {
        try {
            String carNumber = "UP12 AN3456";
            parkingLotService.parkTheCar(carNumber);
            parkingLotService.parkTheCar(carNumber);
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.CAR_ALREADY_PARKED, exception.exceptionType);
        }
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
    public void givenParkingLot_WhenFullAndQueriedForParkingAvailabilityByOwner_ShouldReturnTrue() {
        parkingLotService.registerObserver(owner);
        String[] carNumber = {"UP12 AN3456", "UP34 AN5678", "UP56 QW1234", "UP56 QW1235", "UP56 QW1236"};
        IntStream.rangeClosed(0, 4).forEachOrdered(i -> parkingLotService.parkTheCar(carNumber[i]));
        Assert.assertTrue(owner.isParkingLotFull());
    }

    @Test
    public void givenCarsToPark_WhenAskedToParkBeyondSize_ShouldThrowAnException() {
        try {
            String[] carNumber = {"UP12 AN3456", "UP34 AN5678", "UP56 QW1234", "UP56 QW1235", "UP56 QW1236",
                    "UP56 QW1237"};
            IntStream.rangeClosed(0, 5).forEachOrdered(i -> parkingLotService.parkTheCar(carNumber[i]));
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.PARKING_FULL, exception.exceptionType);
        }
    }

    @Test
    public void givenCarToUnPark_WhenCarNotPresent_ShouldThrowAnException() {
        try {
            parkingLotService.unParkTheCar("UP12 AB3456");
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.CAR_NOT_PRESENT, exception.exceptionType);
        }
    }

    @Test
    public void givenParkingLot_WhenFullAndQueriedByAirportAuthority_ShouldReturnTrue() {
        parkingLotService.registerObserver(airportSecurity);
        String[] carNumber = {"UP12 AN3456", "UP34 AN5678", "UP56 QW1234", "UP56 QW1235", "UP56 QW1236"};
        IntStream.rangeClosed(0, 4).forEachOrdered(i -> parkingLotService.parkTheCar(carNumber[i]));
        Assert.assertTrue(airportSecurity.isParkingLotFull());
    }

    @Test
    public void givenParkingLot_WhenParkingLotNotFullAndCheckedByOwner_ShouldReturnFalse() {
        parkingLotService.registerObserver(owner);
        String[] carNumber = {"UP12 AN3456", "UP34 AN5678", "UP56 QW1234"};
        IntStream.rangeClosed(0, 2).forEachOrdered(i -> parkingLotService.parkTheCar(carNumber[i]));
        Assert.assertFalse(owner.isParkingLotFull());
    }

    @Test
    public void givenCarNumber_WhenParkedAndQueriedAboutSlot_ShouldReturnSlotNumber() {
        String[] carNumber = {"UP12 AN3456", "UP34 AN5678", "UP56 QW1234"};
        IntStream.rangeClosed(0, 2).forEachOrdered(i -> parkingLotService.parkTheCar(carNumber[i]));
        int slot = parkingLotService.getSlotOfCar(carNumber[2]);
        Assert.assertEquals(3, slot);
    }

    @Test
    public void givenCarNumber_WhenNotParkedAndQueriedAboutSlot_ShouldThrowAnException() {
        try {
            String[] carNumber = {"UP12 AN3456", "UP34 AN5678", "UP56 QW1234"};
            IntStream.rangeClosed(0, 2).forEachOrdered(i -> parkingLotService.parkTheCar(carNumber[i]));
            parkingLotService.getSlotOfCar("UP11 AA1111");
        } catch (ParkingLotServiceException e) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.CAR_NOT_PRESENT, e.exceptionType);
        }
    }
}