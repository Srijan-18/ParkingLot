package parkinglot.exception;

public class ParkingLotServiceException extends RuntimeException {
    public enum ExceptionType {
        VEHICLE_NOT_PRESENT, VEHICLE_ALREADY_PARKED, NO_SPACE_FOR_LARGE_VEHICLE, PARKING_FULL
    }

    public ExceptionType exceptionType;

    public ParkingLotServiceException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}