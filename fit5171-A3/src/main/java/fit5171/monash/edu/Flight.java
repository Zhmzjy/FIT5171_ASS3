package fit5171.monash.edu;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Objects;

public class Flight {
    private int flightID;
    private String departTo;
    private String departFrom;
    private String code;
    private String company;
    private Timestamp dateFrom;
    private Timestamp dateTo;
    Airplane airplane;

    // Set to keep track of added flights for duplicate check
    private static final HashSet<String> flightRegistry = new HashSet<>();

    public Flight() {}

    public Flight(int flight_id, String departTo, String departFrom, String code, String company, Timestamp dateFrom, Timestamp dateTo, Airplane airplane) {
        validateNotNull(departTo, "Departure destination");
        validateNotNull(departFrom, "Departure origin");
        validateNotNull(code, "Flight code");
        validateNotNull(company, "Company");
        validateNotNull(dateFrom, "Departure time");
        validateNotNull(dateTo, "Arrival time");
        validateNotNull(airplane, "Airplane");

        validateDateFormat(dateFrom.toString());
        validateDateFormat(dateTo.toString());

        String flightKey = generateFlightKey(flight_id, code);
        if (flightRegistry.contains(flightKey)) {
            throw new IllegalArgumentException("Flight with this ID and code already exists.");
        }
        flightRegistry.add(flightKey);

        this.flightID = flight_id;
        this.departTo = departTo;
        this.departFrom = departFrom;
        this.code = code;
        this.company = company;
        this.airplane = airplane;
        this.dateTo = dateTo;
        this.dateFrom = dateFrom;
    }

    private void validateNotNull(Object value, String fieldName) {
        if (value == null || value.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
        }
    }

    private void validateDateFormat(String timestamp) {
        try {
            // Expecting: 2024-05-01 15:30:00.0 or similar
            String[] parts = timestamp.split(" ");
            if (parts.length != 2) throw new IllegalArgumentException("Timestamp must contain both date and time.");

            String datePart = parts[0];
            String timePart = parts[1];

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss[.S]");

            dateFormat.parse(datePart);
            timeFormat.parse(timePart);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date/time must be in proper format: YYYY-MM-DD and HH:MM:SS");
        }
    }

    private String generateFlightKey(int flightID, String code) {
        return flightID + "-" + code;
    }

    // Existing getters/setters - unchanged
    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightid) {
        this.flightID = flightid;
    }

    public String getDepartTo() {
        return departTo;
    }

    public void setDepartTo(String departTo) {
        this.departTo = departTo;
    }

    public String getDepartFrom() {
        return departFrom;
    }

    public void setDepartFrom(String departFrom) {
        this.departFrom = departFrom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Timestamp getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Timestamp dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Timestamp getDateTo() {
        return dateTo;
    }

    public void setDateTo(Timestamp dateTo) {
        this.dateTo = dateTo;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public String toString() {
        return "Flight{" + airplane.toString() +
                ", date to=" + getDateTo() + ", " + '\'' +
                ", date from='" + getDateFrom() + '\'' +
                ", depart from='" + getDepartFrom() + '\'' +
                ", depart to='" + getDepartTo() + '\'' +
                ", code=" + getCode() + '\'' +
                ", company=" + getCompany() + '\'' +
                ", code=" + getCode() + '\'' +
                '}';
    }
}