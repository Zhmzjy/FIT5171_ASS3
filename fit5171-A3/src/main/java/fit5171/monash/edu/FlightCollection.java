package fit5171.monash.edu;

import java.util.ArrayList;

public class FlightCollection {

    private static ArrayList<Flight> flights = new ArrayList<>();

    public static ArrayList<Flight> getFlights() {
        return flights;
    }

    public static void addFlight(Flight flight) {
        if (isValidFlight(flight)) {
            flights.add(flight);
        } else {
            throw new IllegalArgumentException("Invalid flight details provided.");
        }
    }

    public static void addFlights(ArrayList<Flight> flightsToAdd) {
        for (Flight flight : flightsToAdd) {
            addFlight(flight);
        }
    }

    public static Flight getFlightInfo(String city1, String city2) {
        for (Flight flight : flights) {
            if (isValidCity(city1) && isValidCity(city2) &&
                    flight.getDepartFrom().equalsIgnoreCase(city1) &&
                    flight.getDepartTo().equalsIgnoreCase(city2)) {
                return flight;
            }
        }
        return null;
    }

    public static Flight getFlightInfo(String city) {
        if (!isValidCity(city)) return null;
        for (Flight flight : flights) {
            if (flight.getDepartTo().equalsIgnoreCase(city)) {
                return flight;
            }
        }
        return null;
    }

    public static Flight getFlightInfo(int flight_id) {
        for (Flight flight : flights) {
            if (flight.getFlightID() == flight_id) {
                return flight;
            }
        }
        return null;
    }

    private static boolean isValidFlight(Flight flight) {
        return flight != null &&
                flight.getFlightID() > 0 &&
                isValidCity(flight.getDepartFrom()) &&
                isValidCity(flight.getDepartTo()) &&
                flight.getCode() != null && !flight.getCode().trim().isEmpty() &&
                flight.getCompany() != null && !flight.getCompany().trim().isEmpty() &&
                flight.getDateFrom() != null &&
                flight.getDateTo() != null &&
                flight.getAirplane() != null;
    }

    private static boolean isValidCity(String city) {
        return city != null && city.matches("^[A-Za-z ]+$");
    }
}
