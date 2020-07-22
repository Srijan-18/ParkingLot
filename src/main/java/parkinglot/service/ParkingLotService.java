package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ParkingLotService {

    private Map<String, String> parkedCars;
    private int parkingLotSize;
    private boolean isParkingLotFull;
    private List<IObserver> observersList ;

    public ParkingLotService() {
        parkedCars = new HashMap<>();
        isParkingLotFull = false;
        observersList = new LinkedList<>();
    }

    public void parkTheCar(String carNumber) {
        if(this.isCarPresent(carNumber))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.CAR_ALREADY_PARKED,
                                                 "CAR ALREADY PARKED" + carNumber );
        if(isParkingLotFull)
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.PARKING_FULL,
                                                 "NO MORE SPACE TO PARK " + carNumber );
        parkedCars.put(carNumber, "NON-MEMBER");
        if(parkedCars.size() == parkingLotSize)
            isParkingLotFull = true;
        this.notifyObservers();
    }

    public boolean isCarPresent(String carNumber) {
        return parkedCars.containsKey(carNumber);
    }

    public void unParkTheCar(String carNumber) {
        if(!this.isCarPresent(carNumber))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.CAR_NOT_PRESENT,
                                                 carNumber + " IS NOT PRESENT IN PARKING LOT.");
        parkedCars.remove(carNumber);
        isParkingLotFull = false;
        this.notifyObservers();
    }

    public void setParkingLotSize(int size) {
        this.parkingLotSize = size;
    }

    private void notifyObservers() {
        for (IObserver observer: observersList) {
            observer.setParkingLotStatus(isParkingLotFull);
        }
    }

    public void registerObserver(IObserver observer) {
        observersList.add(observer);
    }
}