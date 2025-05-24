package fit5171.monash.edu;

public class BuyTicket {

    public void buyTicket(int ticket_id) {
        Ticket validTicket = TicketCollection.getTicketInfo(ticket_id);

        if (validTicket == null) {
            System.out.println("This ticket does not exist.");
            return;
        }

        Flight flight = FlightCollection.getFlightInfo(validTicket.getFlight().getFlightID());
        Airplane airplane = flight.getAirplane();

        Passenger passenger = new Passenger();
        passenger.setFirstName("John");
        passenger.setSecondName("Doe");
        passenger.setAge(30);
        passenger.setGender("Male");
        passenger.setEmail("john.doe@example.com");
        passenger.setPhoneNumber("+61412345678"); // Valid AU format
        passenger.setPassport("AUS1234567");

        validTicket.setPassenger(passenger);
        validTicket.setTicketStatus(true);

        if (validTicket.getClassVip()) {
            airplane.setBusinessSitsNumber(airplane.getBusinessSitsNumber() - 1);
        } else {
            airplane.setEconomySitsNumber(airplane.getEconomySitsNumber() - 1);
        }

        validTicket.setPrice(200); // triggers service tax and discount
        System.out.println("Total bill: " + validTicket.getPrice() + "\n");

        passenger.setCardNumber("1234567890123456");
        passenger.setSecurityCode(123);
    }

    public void buyTicket(int ticket_id_first, int ticket_id_second) {
        Ticket validTicketFirst = TicketCollection.getTicketInfo(ticket_id_first);
        Ticket validTicketSecond = TicketCollection.getTicketInfo(ticket_id_second);

        if (validTicketFirst == null || validTicketSecond == null) {
            System.out.println("One or both tickets are invalid.");
            return;
        }

        Flight flight_first = FlightCollection.getFlightInfo(validTicketFirst.getFlight().getFlightID());
        Flight flight_second = FlightCollection.getFlightInfo(validTicketSecond.getFlight().getFlightID());

        Airplane airplane_first = flight_first.getAirplane();
        Airplane airplane_second = flight_second.getAirplane();

        Passenger passenger = new Passenger();
        passenger.setFirstName("John");
        passenger.setSecondName("Doe");
        passenger.setAge(30);
        passenger.setGender("Male");
        passenger.setEmail("john.doe@example.com");
        passenger.setPhoneNumber("+61412345678");
        passenger.setPassport("AUS1234567");

        validTicketFirst.setPassenger(passenger);
        validTicketFirst.setTicketStatus(true);
        if (validTicketFirst.getClassVip()) {
            airplane_first.setBusinessSitsNumber(airplane_first.getBusinessSitsNumber() - 1);
        } else {
            airplane_first.setEconomySitsNumber(airplane_first.getEconomySitsNumber() - 1);
        }

        validTicketSecond.setPassenger(passenger);
        validTicketSecond.setTicketStatus(true);
        if (validTicketSecond.getClassVip()) {
            airplane_second.setBusinessSitsNumber(airplane_second.getBusinessSitsNumber() - 1);
        } else {
            airplane_second.setEconomySitsNumber(airplane_second.getEconomySitsNumber() - 1);
        }

        double totalPrice = validTicketFirst.getPrice() + validTicketSecond.getPrice();
        System.out.println("Total bill: " + totalPrice);

        passenger.setCardNumber("1234567890123456");
        passenger.setSecurityCode(123);
    }
}
