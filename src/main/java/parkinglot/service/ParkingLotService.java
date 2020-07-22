package parkinglot.service;

import parkinglot.exception.ParkingLotServiceException;
import parkinglot.observer.Owner;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

public class ParkingLotService {

    private Map<Integer, String> parkedCars;
    private boolean isParkingLotFull;
    private List<IObserver> observersList;
    private ParkingAttendant parkingAttendant;
    public Map<String, String> timeOfParking;
    private Owner owner = new Owner();

    public ParkingLotService(int parkingLotSize) {
        parkedCars = new HashMap<>();
        IntStream.rangeClosed(1, parkingLotSize).forEachOrdered(i -> parkedCars.put(i, String.valueOf(i)));
        isParkingLotFull = false;
        timeOfParking = new HashMap<>();
        observersList = new LinkedList<>();
        parkingAttendant = new ParkingAttendant();
    }

    public void parkTheCar(String carNumber) {
        parkedCars = parkingAttendant.parkTheCar(carNumber, parkedCars);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        timeOfParking.put(carNumber, formatter.format(date));
        isParkingLotFull = true;
        for (Integer i = 1; i <= parkedCars.size(); i++)
            if (parkedCars.get(i).equals(String.valueOf(i)))
                isParkingLotFull = false;
        this.notifyObservers();
        owner.setParkingTimeMap(timeOfParking);
    }

    public boolean isCarPresent(String carNumber) {
        return parkingAttendant.isCarPresent(carNumber, parkedCars);
    }

    public void unParkTheCar(String carNumber) {
        Integer slot = this.getSlotOfCar(carNumber);
        parkedCars.put(slot, String.valueOf(slot));
        timeOfParking.remove(carNumber);
        isParkingLotFull = false;
        this.notifyObservers();
        owner.setParkingTimeMap(timeOfParking);
    }

    private void notifyObservers() {
        observersList.forEach(observer -> observer.setParkingLotStatus(isParkingLotFull));
    }

    public Integer getSlotOfCar(String carNumber) {
        if (!this.isCarPresent(carNumber))
            throw new ParkingLotServiceException(ParkingLotServiceException.ExceptionType.CAR_NOT_PRESENT,
                    carNumber + " IS NOT PRESENT IN PARKING LOT.");
        Iterator<Map.Entry<Integer, String>> mapIterator = parkedCars.entrySet().iterator();
        Integer slot = 0;
        while (mapIterator.hasNext()) {
            Map.Entry me = mapIterator.next();
            if (me.getValue().equals(carNumber))
                slot = (Integer) me.getKey();
        }
        return slot;
    }

    public void registerObserver(IObserver observer) {
        observersList.add(observer);
    }
}