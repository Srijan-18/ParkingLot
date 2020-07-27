package parkinglot.exception;

import java.util.function.Supplier;

public class ParkingLotServiceException extends RuntimeException {
    public enum ExceptionType {
        VEHICLE_NOT_PRESENT, VEHICLE_ALREADY_PARKED, NO_SPACE_FOR_LARGE_VEHICLE, NO_SUCH_VEHICLE_PRESENT, EMPTY_PARKING_LOT, PARKING_FULL
    }

    public ExceptionType exceptionType;

    public ParkingLotServiceException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}