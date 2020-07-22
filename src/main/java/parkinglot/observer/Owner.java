package parkinglot.observer;

import parkinglot.exception.ParkingLotServiceException;
import parkinglot.service.IObserver;

import java.util.HashMap;
import java.util.Map;

public class Owner implements IObserver {
    private boolean isParkingLotFull;
    public static Map<String, String> timeOfParking;

    @Override
    public void setParkingLotStatus(boolean isParkingLotFull) {
        this.isParkingLotFull = isParkingLotFull;
        timeOfParking = new HashMap<>();
    }

    @Override
    public boolean isParkingLotFull() {
        return isParkingLotFull;
    }

    public Integer getSlotToPark(Map<Integer, String> parkedCars) {
        for (Integer i = 1; i <= parkedCars.size(); i++)
            if (parkedCars.get(i).equals(String.valueOf(i)))
                return i;
        return null;
    }

    public void setParkingTimeMap(Map<String, String> parkingTimeMap) {
        timeOfParking = parkingTimeMap;
    }

    public String getTimeOfCar(String carNumber) {
        if(!timeOfParking.containsKey(carNumber))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.CAR_NOT_PRESENT,
                                                 carNumber + " IS NOT PRESENT IN PARKING LOT");
        return timeOfParking.get(carNumber);
    }
}
