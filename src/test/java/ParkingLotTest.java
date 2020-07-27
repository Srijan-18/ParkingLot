import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parkinglot.exception.ParkingLotServiceException;
import parkinglot.model.Slot;
import parkinglot.model.Vehicle;
import parkinglot.observers.AirportSecurity;
import parkinglot.observers.Owner;
import parkinglot.service.ParkingLotService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
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
        Vehicle vehicle = new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleSize.SMALL,
                Vehicle.VehicleColour.NOT_SPECIFIED, Vehicle.VehicleCompany.NOT_SPECIFIED,"UP-01-KK-1111");
        parkingLotService.parkTheVehicle(vehicle);
        boolean status = parkingLotService.isVehiclePresent(vehicle);
        Assert.assertTrue(status);
    }

    @Test
    public void givenAVehicleParked_WhenUnParked_ShouldReturnFalse() {
        Vehicle vehicle = new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleSize.SMALL,
                Vehicle.VehicleColour.NOT_SPECIFIED, Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-01-KK-1111");
        parkingLotService.parkTheVehicle(vehicle);
        parkingLotService.unParkTheVehicle(vehicle);
        boolean status = parkingLotService.isVehiclePresent(vehicle);
        Assert.assertFalse(status);
    }

    @Test
    public void givenParkingLotWithItsSize_WhenFullyOccupiedAndQueriedByOwner_ShouldReturnTrue() {
        Vehicle[] vehicles = new Vehicle[3];
        IntStream.range(0, 3).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        boolean status = owner.getParkingLotStatus();
        Assert.assertTrue(status);
    }

    @Test
    public void givenVehiclesToPark_WhenAskedToParkBeyondSize_ShouldThrowAnException() {
        try {
            Vehicle[] vehicles = new Vehicle[4];
            IntStream.range(0, 4).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                    Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                    Vehicle.VehicleCompany.NOT_SPECIFIED,"UP-"+ (index + 1) +"-KK-1111"));
            Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.PARKING_FULL, exception.exceptionType);
        }
    }

    @Test
    public void givenVehicleToUnPark_WhenNotPresent_ShouldThrowAnException() {
        try {
            parkingLotService.unParkTheVehicle(new Vehicle(Vehicle.DriverCategory.NORMAL,
                    Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                    Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-01-KK-1111"));
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT, exception.exceptionType);
        }
    }

    @Test
    public void givenParkingLot_WhenFullAndQueriedForFullParkingLotByAirportSecurity_ShouldReturnTrue() {
        Vehicle[] vehicles = new Vehicle[3];
        IntStream.range(0, 3).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        boolean status = airportSecurity.getParkingLotStatus();
        Assert.assertTrue(status);
    }

    @Test
    public void givenParkingLot_WhenParkingLotNotFullAndCheckedByOwner_ShouldReturnFalse() {
        Vehicle[] vehicles = new Vehicle[2];
        IntStream.range(0, 2).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        boolean status = owner.getParkingLotStatus();
        Assert.assertFalse(status);
    }

    @Test
    public void givenAParkedVehicle_WhenQueriedForSlotNumber_ShouldReturnSlotNumber() {
        Vehicle[] vehicles = new Vehicle[3];
        IntStream.range(0, 3).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
        Arrays.stream(vehicles).forEachOrdered(vehicle -> parkingLotService.parkTheVehicle(vehicle));
        Assert.assertEquals("Parking Lot:1 Slot:2", parkingLotService.getSlotOfParkedVehicle(vehicles[1]));
    }

    @Test
    public void givenAVehicleNotPresentInParkingLot_WhenQueriedForSlotNumber_ShouldThrowAnException() {
        try {
            parkingLotService.getSlotOfParkedVehicle(new Vehicle(Vehicle.DriverCategory.NORMAL,
                    Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                    Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-01-KK-1111"));
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT, exception.exceptionType);
        }
    }

    @Test
    public void givenAVehicle_WhenAlreadyParked_ShouldThrowAnException() {
        try {
            Vehicle vehicle = new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleSize.SMALL,
                    Vehicle.VehicleColour.NOT_SPECIFIED, Vehicle.VehicleCompany.NOT_SPECIFIED,
                    "UP-01-KK-1111");
            parkingLotService.parkTheVehicle(vehicle);
            parkingLotService.parkTheVehicle(vehicle);
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_ALREADY_PARKED,
                    exception.exceptionType);
        }
    }

    @Test
    public void givenVehicleToPark_WhenParkedAndQueriedForTimeOfParking_ShouldReturnCurrentTime() {
        Vehicle vehicle = new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleSize.SMALL,
                Vehicle.VehicleColour.NOT_SPECIFIED, Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-01-KK-1111");
        parkingLotService.parkTheVehicle(vehicle);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
        LocalDateTime now = LocalDateTime.now();
        Assert.assertEquals(dateTimeFormatter.format(now), parkingLotService.getTimeOfParkingForVehicle(vehicle));
    }

    @Test
    public void givenVehicleNotPresentInParkingLot_WhenQueriedForTime_ShouldThrowAnException() {
        try {
            parkingLotService.getTimeOfParkingForVehicle(new Vehicle(Vehicle.DriverCategory.NORMAL,
                    Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                    Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-01-KK-1111"));
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.VEHICLE_NOT_PRESENT, exception.exceptionType);
        }
    }

    @Test
    public void givenMultipleParkingLotsAndVehicles_WhenParked_ShouldDistributeThemEvenly() {
        ParkingLotService lotService = new ParkingLotService(3, 3);
        Vehicle[] vehicles = new Vehicle[4];
        IntStream.range(0, 4).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
        Arrays.stream(vehicles).forEachOrdered(lotService::parkTheVehicle);
        Assert.assertEquals("Parking Lot:1 Slot:2", lotService.getSlotOfParkedVehicle(vehicles[3]));
    }

    @Test
    public void givenAVehicle_WhenAlreadyParkedInAnyOfLots_ShouldThrowAnException() {
        try {
            ParkingLotService parkingLotService = new ParkingLotService(3, 3);
            Vehicle[] vehicles = new Vehicle[4];
            IntStream.range(0, 4).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                    Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                    Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
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
            Vehicle[] vehicles = new Vehicle[5];
            IntStream.range(0, 5).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                    Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                    Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
            Arrays.stream(vehicles).forEachOrdered(parkingLotService::parkTheVehicle);
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.PARKING_FULL, exception.exceptionType);
        }
    }

    @Test
    public void givenHandicappedDriver_WhenToPark_ShouldBeGivenNearestSlot() {
        ParkingLotService parkingLotService = new ParkingLotService(3, 3);
        Vehicle[] vehicles = {new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleSize.SMALL,
                Vehicle.VehicleColour.NOT_SPECIFIED, Vehicle.VehicleCompany.NOT_SPECIFIED,
                "UP-01-KK-1111"),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleSize.SMALL,
                        Vehicle.VehicleColour.NOT_SPECIFIED, Vehicle.VehicleCompany.NOT_SPECIFIED,
                        "UP-02-KK-1111"),
                new Vehicle(Vehicle.DriverCategory.HANDICAPPED, Vehicle.VehicleSize.SMALL,
                        Vehicle.VehicleColour.NOT_SPECIFIED, Vehicle.VehicleCompany.NOT_SPECIFIED,
                        "UP-03-KK-1111"),
                new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleSize.SMALL,
                        Vehicle.VehicleColour.NOT_SPECIFIED, Vehicle.VehicleCompany.NOT_SPECIFIED,
                        "UP-04-KK-1111")};
        Arrays.stream(vehicles).forEachOrdered(parkingLotService::parkTheVehicle);
        Assert.assertEquals("Parking Lot:1 Slot:2", parkingLotService.getSlotOfParkedVehicle(vehicles[2]));
    }

    @Test
    public void givenOnlyHandicappedDrivers_WhenToPark_ShouldParkSerially() {
        ParkingLotService parkingLotService = new ParkingLotService(2, 3);
        Vehicle[] vehicles = new Vehicle[5];
        IntStream.range(0, 4).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.HANDICAPPED,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
        vehicles[4] = new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleSize.SMALL,
                Vehicle.VehicleColour.NOT_SPECIFIED, Vehicle.VehicleCompany.NOT_SPECIFIED,
                "UP-"+ 5 +"-KK-1111");
        Arrays.stream(vehicles).forEachOrdered(parkingLotService::parkTheVehicle);
        Assert.assertEquals("Parking Lot:3 Slot:1", parkingLotService.getSlotOfParkedVehicle(vehicles[4]));
    }

    @Test
    public void givenMultipleLargeVehiclesToPark_WhenNoTwoConsecutiveSlotsAvailable_ShouldThrowAnException() {
        ParkingLotService parkingLotService = new ParkingLotService(2, 2);
        Vehicle[] vehicles = new Vehicle[3];
        IntStream.range(0, 3).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.LARGE, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
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
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
        IntStream.range(0, 12).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        IntStream.rangeClosed(3, 5).forEachOrdered(index -> parkingLotService.unParkTheVehicle(vehicles[index]));
        IntStream.rangeClosed(7, 9).forEachOrdered(index -> parkingLotService.unParkTheVehicle(vehicles[index]));
        parkingLotService.unParkTheVehicle(vehicles[11]);
        Vehicle largeVehicleWithHandicappedDriver = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.LARGE, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-23-KK-1111");
        parkingLotService.parkTheVehicle(largeVehicleWithHandicappedDriver);
        Assert.assertEquals("Parking Lot:3 Slot:2", parkingLotService
                .getSlotOfParkedVehicle(largeVehicleWithHandicappedDriver));
    }

    @Test
    public void givenLargeVehicleWithHandicappedDriver_WhenToPark_ShouldParkInNearestLotHavingConsecutiveEmptySlots() {
        ParkingLotService parkingLotService = new ParkingLotService(4, 3);
        Vehicle[] vehicles = new Vehicle[12];
        IntStream.range(0, 12).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
        IntStream.range(0, 12).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        IntStream.rangeClosed(3, 5).forEachOrdered(index -> parkingLotService.unParkTheVehicle(vehicles[index]));
        IntStream.rangeClosed(7, 9).forEachOrdered(index -> parkingLotService.unParkTheVehicle(vehicles[index]));
        parkingLotService.unParkTheVehicle(vehicles[11]);
        Vehicle largeVehicleWithHandicappedDriver = new Vehicle(Vehicle.DriverCategory.HANDICAPPED,
                Vehicle.VehicleSize.LARGE, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-23-KK-1111");
        parkingLotService.parkTheVehicle(largeVehicleWithHandicappedDriver);
        Assert.assertEquals("Parking Lot:2 Slot:2", parkingLotService
                .getSlotOfParkedVehicle(largeVehicleWithHandicappedDriver));
    }

    @Test
    public void givenParkedVehicles_WhenQueriedForLocationOfWhiteVehicles_ShouldReturnTheirLocations() {
        ParkingLotService parkingLotService = new ParkingLotService(3, 2);
        Vehicle[] vehicles = new Vehicle[4];
        vehicles[0] = new Vehicle(Vehicle.DriverCategory.NORMAL, Vehicle.VehicleSize.SMALL,
                Vehicle.VehicleColour.NOT_SPECIFIED, Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-01-KK-1111");
        IntStream.range(1, 3).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.WHITE, Vehicle.VehicleCompany.NOT_SPECIFIED,
                "UP-"+ (index + 1) +"-KK-1111" ));
        vehicles[3] = new Vehicle(Vehicle.DriverCategory.HANDICAPPED,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.WHITE, Vehicle.VehicleCompany.NOT_SPECIFIED,
                "UP-51-KK-1111");
        IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        String[] locationsOfWhiteVehicles = parkingLotService.getLocationOfVehiclesOfParticularColour
                (Vehicle.VehicleColour.WHITE);
        Assert.assertEquals("Parking Lot:1 Slot:3", locationsOfWhiteVehicles[1]);
    }

    @Test
    public void givenParkedVehicles_WhenNoWhiteVehicleParkedAndQueriedForWhiteVehiclesLocations_ShouldThrowAnException() {
        ParkingLotService parkingLotService = new ParkingLotService(3, 2);
        Vehicle[] vehicles = new Vehicle[6];
        IntStream.range(0, 6).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.NOT_SPECIFIED,
                Vehicle.VehicleCompany.NOT_SPECIFIED, "UP-"+ (index + 1) +"-KK-1111"));
        IntStream.range(0, 6).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        try {
            parkingLotService.getLocationOfVehiclesOfParticularColour(Vehicle.VehicleColour.WHITE);
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.NO_SUCH_VEHICLE_PRESENT,
                    exception.exceptionType);
        }
    }

    @Test
    public void givenParkedVehicles_WhenQueriedAboutBlueToyotaVehicles_ShouldReturnTheirDetails() {
        ParkingLotService parkingLotService = new ParkingLotService(2, 2);
        String[] attendantNames = {"Attendant1", "Attendant2", "Attendant3", "Attendant4"};
        IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.addParkingAttendant(attendantNames[index]));
        Vehicle[] vehicles = new Vehicle[4];
        IntStream.range(0, 3).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.WHITE, Vehicle.VehicleCompany.NOT_SPECIFIED,
                "UP-"+ (index + 1) +"-KK-1111" ));
        vehicles[3] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.BLUE, Vehicle.VehicleCompany.TOYOTA,
                "UP-"+ 4 +"-KK-1111");
        IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        List<Slot> detailsOfBlueToyotaVehicles = parkingLotService.getDetailsOfBlueToyotaVehiclesParked();
        Assert.assertEquals(Vehicle.VehicleCompany.TOYOTA, detailsOfBlueToyotaVehicles.get(0).getVehicle().vehicleCompany);
        Assert.assertEquals(Vehicle.VehicleColour.BLUE, detailsOfBlueToyotaVehicles.get(0).getVehicle().vehicleColour);
    }

    @Test
    public void givenParkedVehicles_WhenQueriedAboutBlueToyotaVehicleParkedByDriver_ShouldReturnDriverAsAttendantName() {
        ParkingLotService parkingLotService = new ParkingLotService(2, 2);
        Vehicle[] vehicles = new Vehicle[4];
        IntStream.range(0, 3).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.WHITE, Vehicle.VehicleCompany.NOT_SPECIFIED,
                "UP-"+ (index + 1) +"-KK-1111" ));
        vehicles[3] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.BLUE, Vehicle.VehicleCompany.TOYOTA,
                "UP-"+ 4 +"-KK-1111");
        IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        List<Slot> detailsOfBlueToyotaVehicles = parkingLotService.getDetailsOfBlueToyotaVehiclesParked();
        Assert.assertEquals("Driver Of Vehicle", detailsOfBlueToyotaVehicles.get(0).getAttendantName());
    }

    @Test
    public void givenParkedVehiclesAndQueriedAboutBlueToyotaVehicleParked_WhenNotPresent_ShouldThrowAnException() {
        try {
            ParkingLotService parkingLotService = new ParkingLotService(2, 2);
            String[] attendantNames = {"Attendant1", "Attendant2", "Attendant3", "Attendant4"};
            IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.addParkingAttendant(attendantNames[index]));
            Vehicle[] vehicles = new Vehicle[4];
            IntStream.range(0, 4).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                    Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.WHITE, Vehicle.VehicleCompany.NOT_SPECIFIED,
                    "UP-" + (index + 1) + "-KK-1111"));
            IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
            parkingLotService.getDetailsOfBlueToyotaVehiclesParked();
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.NO_SUCH_VEHICLE_PRESENT,
                    exception.exceptionType);
        }
    }

    @Test
    public void givenParkedVehicles_WhenQueriedAboutBMWVehicles_ShouldReturnTheirDetails() {
        ParkingLotService parkingLotService = new ParkingLotService(2, 2);
        String[] attendantNames = {"Attendant1", "Attendant2", "Attendant3", "Attendant4"};
        IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.addParkingAttendant(attendantNames[index]));
        Vehicle[] vehicles = new Vehicle[4];
        IntStream.range(0, 3).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.WHITE, Vehicle.VehicleCompany.NOT_SPECIFIED,
                "UP-"+ (index + 1) +"-KK-1111" ));
        vehicles[3] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.BLUE, Vehicle.VehicleCompany.BMW,
                "UP-"+ 4 +"-KK-1111");
        IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        List<Slot> detailsOfBMWVehiclesParked = parkingLotService.getDetailsOfBMWVehicles();
        Assert.assertEquals(Vehicle.VehicleCompany.BMW, detailsOfBMWVehiclesParked.get(0).getVehicle().vehicleCompany);


    }

    @Test
    public void givenParkedVehiclesAndQueriedAboutBMWVehiclesParked_WhenNotPresent_ShouldThrowAnException() {
       try {
           ParkingLotService parkingLotService = new ParkingLotService(2, 2);
           String[] attendantNames = {"Attendant1", "Attendant2", "Attendant3", "Attendant4"};
           IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.addParkingAttendant(attendantNames[index]));
           Vehicle[] vehicles = new Vehicle[4];
           IntStream.range(0, 4).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                   Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.WHITE, Vehicle.VehicleCompany.NOT_SPECIFIED,
                   "UP-" + (index + 1) + "-KK-1111"));
           IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
           parkingLotService.getDetailsOfBMWVehicles();
       } catch (ParkingLotServiceException exception) {
           Assert.assertEquals(ParkingLotServiceException.ExceptionType.NO_SUCH_VEHICLE_PRESENT,
                   exception.exceptionType);
       }
    }

    @Test
    public void givenParkedVehicles_WhenQueriedAboutVehiclesParkedInLast30Minutes_ShouldReturnTheirDetails() {
        ParkingLotService parkingLotService = new ParkingLotService(2, 2);
        String[] attendantNames = {"Attendant1", "Attendant2", "Attendant3", "Attendant4"};
        IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.addParkingAttendant(attendantNames[index]));
        Vehicle[] vehicles = new Vehicle[4];
        IntStream.range(0, 3).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.WHITE, Vehicle.VehicleCompany.NOT_SPECIFIED,
                "UP-"+ (index + 1) +"-KK-1111" ));
        vehicles[3] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.BLUE, Vehicle.VehicleCompany.BMW,
                "UP-"+ 4 +"-KK-1111");
        IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        List<Slot> detailsOfVehiclesParkedInLast30Minutes = parkingLotService
                .getDetailsOfVehiclesParkedInLast30Minutes();
        Assert.assertEquals(4, detailsOfVehiclesParkedInLast30Minutes.size());
    }

    @Test
    public void givenZeroParkedVehicles_WhenQueriedAboutVehiclesParkedInLast30Minutes_ShouldThrowAnException() {
        ParkingLotService parkingLotService = new ParkingLotService(2, 2);
        try {
                parkingLotService.getDetailsOfVehiclesParkedInLast30Minutes();
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.NO_SUCH_VEHICLE_PRESENT,
                    exception.exceptionType);
        }
    }

    @Test
    public void givenParkedVehicles_WhenQueriedForSmallVehiclesWithHandicappedDriversInLots2And4_ShouldReturnTheirDetails() {
        ParkingLotService parkingLotService = new ParkingLotService(2, 4);
        String[] attendantNames = {"Attendant1", "Attendant2", "Attendant3", "Attendant4"};
        IntStream.range(0, 4).forEachOrdered(index -> parkingLotService.addParkingAttendant(attendantNames[index]));
        Vehicle[] vehicles = new Vehicle[8];
        IntStream.range(0, 3).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.HANDICAPPED,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.WHITE, Vehicle.VehicleCompany.NOT_SPECIFIED,
                "UP-"+ (index + 1) +"-KK-1111" ));
        IntStream.range(3, 7).forEachOrdered(index -> vehicles[index] = new Vehicle(Vehicle.DriverCategory.NORMAL,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.WHITE, Vehicle.VehicleCompany.NOT_SPECIFIED,
                "UP-"+ (index + 1) +"-KK-1111" ));
        vehicles[7] = new Vehicle(Vehicle.DriverCategory.HANDICAPPED,
                Vehicle.VehicleSize.SMALL, Vehicle.VehicleColour.BLUE, Vehicle.VehicleCompany.BMW,
                "UP-"+ 8 +"-KK-1111");
        IntStream.range(0, 8).forEachOrdered(index -> parkingLotService.parkTheVehicle(vehicles[index]));
        List<Slot> detailsOfSmallVehiclesWithGivenConditions = parkingLotService
                .getDetailsOfSmallVehiclesWithHandicappedDriversInLot2and4();
        Assert.assertEquals(vehicles[2], detailsOfSmallVehiclesWithGivenConditions.get(0).getVehicle());
        Assert.assertEquals(2, detailsOfSmallVehiclesWithGivenConditions.size());

    }

    @Test
    public void givenEmptyParkingLot_WhenQueriedForSmallVehiclesWithHandicappedDriversInLots2And4_ShouldThrowException() {
        ParkingLotService parkingLotService = new ParkingLotService(2, 4);
        try {
            parkingLotService.getDetailsOfSmallVehiclesWithHandicappedDriversInLot2and4();
        } catch (ParkingLotServiceException exception) {
            Assert.assertEquals(ParkingLotServiceException.ExceptionType.NO_SUCH_VEHICLE_PRESENT,
                    exception.exceptionType);
        }
    }
}