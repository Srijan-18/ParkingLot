package parkinglot.exception;

public class ParkingLotServiceException extends RuntimeException {
    public enum ExceptionType{
        CAR_NOT_PRESENT, CAR_ALREADY_PARKED, PARKING_FULL
    }

    public ExceptionType exceptionType;

    public ParkingLotServiceException(ExceptionType exceptionType, String message) {
    super(message);
    this.exceptionType = exceptionType;
    }
}