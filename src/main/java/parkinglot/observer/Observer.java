package parkinglot.observer;


public class Observer {
    public AirportSecurity airportSecurity;
    public Owner owner;
    public Observer() {
        airportSecurity = new AirportSecurity();
        owner = new Owner();
    }

    public void notifyAllObservers(boolean isParkingFull) {
        airportSecurity.setParkingLotStatus(isParkingFull);
        owner.setParkingLotStatus(isParkingFull);
    }
}
