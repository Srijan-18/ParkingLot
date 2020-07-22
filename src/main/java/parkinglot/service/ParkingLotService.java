package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;

import java.util.*;
import java.util.stream.IntStream;

public class ParkingLotService {

    private Map<Integer, String> parkedCars;
    private boolean isParkingLotFull;
    private ParkingAttendant parkingAttendant;
    private List<IObserver> observersList;

    public ParkingLotService(int parkingLotSize) {
        parkedCars = new HashMap<>();
        IntStream.rangeClosed(1, parkingLotSize).forEachOrdered(i -> parkedCars.put(i, String.valueOf(i)));
        isParkingLotFull = false;
        observersList = new LinkedList<>();
        parkingAttendant = new ParkingAttendant();
    }

    public void parkTheCar(String carNumber) {
        parkedCars = parkingAttendant.parkTheCar(carNumber , parkedCars);
        isParkingLotFull = true;
        for (Integer i = 1; i <= parkedCars.size(); i++)
                  if (parkedCars.get(i).equals(String.valueOf(i)))
                    isParkingLotFull = false;
        this.notifyObservers();
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
        this.notifyObservers();
    }

    private void notifyObservers() {
        observersList.forEach(observer -> observer.setParkingLotStatus(isParkingLotFull));
    }

    public void registerObserver(IObserver observer) {
        observersList.add(observer);
    }
}