package fit5171.monash.edu;

import java.util.Scanner;

public class ChooseTicket {
    public void chooseTicket() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your departure city: ");
        String city1 = scanner.nextLine();

        System.out.print("Enter your destination city: ");
        String city2 = scanner.nextLine();

        Flight directFlight = FlightCollection.getFlightInfo(city1, city2);

        if (directFlight != null) {
            System.out.println("Direct flight available.");
            TicketCollection.getAllTickets();
            System.out.print("Enter ticket ID to purchase: ");
            int ticket_id = scanner.nextInt();
            new BuyTicket().buyTicket(ticket_id);
            return;
        }

        System.out.println("No direct flight found. Checking for transfer flights...");
        Flight arrivingFlight = FlightCollection.getFlightInfo(city2);

        if (arrivingFlight == null) {
            System.out.println("No arriving flights to " + city2);
            return;
        }

        String connectCity = arrivingFlight.getDepartFrom();
        Flight viaFlight = FlightCollection.getFlightInfo(city1, connectCity);

        if (viaFlight == null) {
            System.out.println("No connecting flights from " + city1 + " to " + connectCity);
            return;
        }

        System.out.println("Transfer route found: " + city1 + " -> " + connectCity + " -> " + city2);
        int idFirst = arrivingFlight.getFlightID();
        int idSecond = viaFlight.getFlightID();

        System.out.println("Enter ticket ID for first leg:");
        int ticket_id_first = scanner.nextInt();

        System.out.println("Enter ticket ID for second leg:");
        int ticket_id_second = scanner.nextInt();

        new BuyTicket().buyTicket(ticket_id_first, ticket_id_second);
    }
}
