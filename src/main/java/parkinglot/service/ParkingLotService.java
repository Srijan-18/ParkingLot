package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;
import parkinglot.observer.Observer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

public class ParkingLotService {

    private Map<Integer, String> parkedCars;
    private boolean isParkingLotFull;
    public Observer observers;
    private ParkingAttendant parkingAttendant;

    public ParkingLotService(int parkingLotSize) {
        parkedCars = new HashMap<>();
        IntStream.rangeClosed(1, parkingLotSize).forEachOrdered(i -> parkedCars.put(i, String.valueOf(i)));
        isParkingLotFull = false;
        observers = new Observer();
        parkingAttendant = new ParkingAttendant();
    }

    public void parkTheCar(String carNumber) {
        parkedCars = parkingAttendant.parkTheCar(carNumber , parkedCars);
        isParkingLotFull = true;
        for (Integer i = 1; i <= parkedCars.size(); i++)
                  if (parkedCars.get(i).equals(String.valueOf(i)))
                    isParkingLotFull = false;
    }

    public boolean isCarPresent(String carNumber) {
        return parkingAttendant.isCarPresent(carNumber, parkedCars);
    }

    public void unParkTheCar(String carNumber) {
        if(!this.isCarPresent(carNumber))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.CAR_NOT_PRESENT,
                                                 carNumber + " IS NOT PRESENT IN PARKING LOT.");
        Iterator<Map.Entry<Integer, String>> mapIterator = parkedCars.entrySet().iterator();
        Integer slot = 0;
        while(mapIterator.hasNext()) {
            Map.Entry me = mapIterator.next();
            if(me.getValue().equals(carNumber))
            slot = (Integer) me.getKey();
        }
        parkedCars.put(slot, String.valueOf(slot));
        isParkingLotFull = false;
    }

    public void notifyObserver() {
        observers.notifyAllObservers(isParkingLotFull);
    }
}