package fit5171.monash.edu;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TicketSystem {

    private Scanner in;
    private Passenger passenger;
    Ticket ticket;
    private Flight flight;

    public TicketSystem() {
        in = new Scanner(System.in);
        passenger = new Passenger();
        ticket = new Ticket();
        flight = new Flight();
    }

    public boolean chooseTicket(String city1, String city2) {
        if (!isValidCity(city1) || !isValidCity(city2)) {
            System.out.println("Invalid city names entered.");
            return false;
        }

        flight = FlightCollection.getFlightInfo(city1, city2);
        if (flight != null) {
            TicketCollection.getAllTickets();
            System.out.println("\nEnter the ID of the ticket you want to choose:");
            int ticket_id = in.nextInt();
            return buyTicket(ticket_id);
        }

        System.out.println("No direct flight found. Checking for transfer route...");
        Flight departToFlight = FlightCollection.getFlightInfo(city2);
        if (departToFlight == null) {
            System.out.println("No flights found to destination " + city2);
            return false;
        }

        String connectCity = departToFlight.getDepartFrom();
        Flight viaFlight = FlightCollection.getFlightInfo(city1, connectCity);

        if (viaFlight == null) {
            System.out.println("No connecting flight from " + city1 + " to " + connectCity);
            return false;
        }

        System.out.println("Transfer route found: " + city1 + " -> " + connectCity + " -> " + city2);
        int idFirst = departToFlight.getFlightID();
        int idSecond = viaFlight.getFlightID();
        return buyTicket(idFirst, idSecond);
    }

    public boolean buyTicket(int ticket_id) {
        Ticket validTicket = TicketCollection.getTicketInfo(ticket_id);

        if (ticket_id <= 0 || validTicket == null) {
            System.out.println("This ticket does not exist or invalid ticket ID.");
            return false;
        }

        if (validTicket.ticketStatus()) {
            System.out.println("This ticket is already booked.");
            return false;
        }

        try {
            collectPassengerData();

            System.out.println("Do you want to purchase?\n1 - YES, 0 - NO");
            int purch = in.nextInt();
            if (purch == 0) {
                return false;
            }

            flight = FlightCollection.getFlightInfo(validTicket.getFlight().getFlightID());
            Airplane airplane = flight.getAirplane();

            ticket = validTicket;
            ticket.setPassenger(passenger);
            ticket.setTicketStatus(true);
            ticket.setPrice(ticket.getPrice()); // Apply discount + tax

            if (ticket.getClassVip()) {
                airplane.setBusinessSitsNumber(airplane.getBusinessSitsNumber() - 1);
            } else {
                airplane.setEconomySitsNumber(airplane.getEconomySitsNumber() - 1);
            }

            System.out.println("Your bill: " + ticket.getPrice() + "\n");

            System.out.println("Enter your card number:");
            String cardNumber = in.next();
            passenger.setCardNumber(cardNumber);

            System.out.println("Enter your security code:");
            int securityCode = in.nextInt();
            passenger.setSecurityCode(securityCode);

            return true;
        } catch (PatternSyntaxException e) {
            System.out.println("Regex error during booking: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid argument during booking: " + e.getMessage());
            return false;
        }


    }

    public boolean buyTicket(int ticket_id_first, int ticket_id_second) {
        Ticket validTicketFirst = TicketCollection.getTicketInfo(ticket_id_first);
        Ticket validTicketSecond = TicketCollection.getTicketInfo(ticket_id_second);

        if (validTicketFirst == null || validTicketSecond == null) {
            System.out.println("This ticket does not exist.");
            return false;
        }

        if (validTicketFirst.ticketStatus() || validTicketSecond.ticketStatus()) {
            System.out.println("One of the tickets is already booked.");
            return false;
        }

        try {
            collectPassengerData();

            System.out.println("Do you want to purchase?\n1 - YES, 0 - NO");
            int purch = in.nextInt();
            if (purch == 0) {
                return false;
            }

            Flight flight_first = FlightCollection.getFlightInfo(validTicketFirst.getFlight().getFlightID());
            Flight flight_second = FlightCollection.getFlightInfo(validTicketSecond.getFlight().getFlightID());

            Airplane airplane_first = flight_first.getAirplane();
            Airplane airplane_second = flight_second.getAirplane();

            validTicketFirst.setPassenger(passenger);
            validTicketFirst.setTicketStatus(true);
            validTicketFirst.setPrice(validTicketFirst.getPrice());

            validTicketSecond.setPassenger(passenger);
            validTicketSecond.setTicketStatus(true);
            validTicketSecond.setPrice(validTicketSecond.getPrice());

            if (validTicketFirst.getClassVip()) {
                airplane_first.setBusinessSitsNumber(airplane_first.getBusinessSitsNumber() - 1);
            } else {
                airplane_first.setEconomySitsNumber(airplane_first.getEconomySitsNumber() - 1);
            }

            if (validTicketSecond.getClassVip()) {
                airplane_second.setBusinessSitsNumber(airplane_second.getBusinessSitsNumber() - 1);
            } else {
                airplane_second.setEconomySitsNumber(airplane_second.getEconomySitsNumber() - 1);
            }

            ticket.setPrice(validTicketFirst.getPrice() + validTicketSecond.getPrice());

            System.out.println("Your bill: " + ticket.getPrice() + "\n");

            System.out.println("Enter your card number:");
            String cardNumber = in.next();
            passenger.setCardNumber(cardNumber);

            System.out.println("Enter your security code:");
            int securityCode = in.nextInt();
            passenger.setSecurityCode(securityCode);

            return true;

        } catch (PatternSyntaxException e) {
            System.out.println("Regex error during booking: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid argument during booking: " + e.getMessage());
            return false;
        }

    }

    public void showTicket() {
        try {
            System.out.println("You have bought a ticket for flight " +
                    ticket.getFlight().getDepartFrom() + " - " + ticket.getFlight().getDepartTo() + "\n\nDetails:");
            System.out.println(ticket.toString());
        } catch (NullPointerException e) {
            System.out.println("No ticket purchased yet.");
        }
    }

    private void collectPassengerData() {
        System.out.println("Enter your First Name: ");
        passenger.setFirstName(in.next());

        System.out.println("Enter your Second Name:");
        passenger.setSecondName(in.next());

        System.out.println("Enter your age:");
        passenger.setAge(in.nextInt());

        System.out.println("Enter your gender:");
        passenger.setGender(in.next());

        System.out.println("Enter your e-mail address:");
        passenger.setEmail(in.next());

        System.out.println("Enter your phone number:");
        passenger.setPhoneNumber(in.next());

        System.out.println("Enter your passport number:");
        passenger.setPassport(in.next());
    }

    private boolean isValidCity(String city) {
        return city != null && city.matches("^[A-Za-z ]+$");
    }
}