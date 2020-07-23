package parkinglot.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingUtility {

    public String getCurrentDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
        LocalDateTime localDateTime = LocalDateTime.now();
        return dateTimeFormatter.format(localDateTime);
    }
}
