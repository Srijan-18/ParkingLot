package parkinglot.observer;

import parkinglot.service.IObserver;

import java.util.Map;

public class Owner implements IObserver {
    private boolean isParkingLotFull;

    @Override
    public void setParkingLotStatus(boolean isParkingLotFull) {
        this.isParkingLotFull = isParkingLotFull;

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
}
