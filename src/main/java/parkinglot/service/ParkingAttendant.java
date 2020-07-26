package parkinglot.service;

import java.util.ArrayList;
import java.util.List;

public class ParkingAttendant {
    List<String> listOfAttendants;

    public ParkingAttendant() {
        listOfAttendants = new ArrayList<>();
    }

    public void addParkingAttendant(String attendantName) {
        listOfAttendants.add(attendantName);
    }

    public String getAttendant() {
        if (listOfAttendants.size() == 0)
            return "Driver Of Vehicle";
        int randomNumber = (int) ((Math.floor(Math.random()*1000)) % listOfAttendants.size());
        return listOfAttendants.get(randomNumber);
    }
}
