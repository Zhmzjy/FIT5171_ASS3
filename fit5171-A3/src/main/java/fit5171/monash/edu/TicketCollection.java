package fit5171.monash.edu;

import java.util.ArrayList;

public class TicketCollection {

    private static ArrayList<Ticket> tickets = new ArrayList<>(); // initialize properly

    // Return the list of tickets
    public static ArrayList<Ticket> getTickets() {
        return tickets;
    }

    // Add multiple tickets to the collection (with validation)
    public static void addTickets(ArrayList<Ticket> tickets_db) {
        for (Ticket t : tickets_db) {
            if (validateTicket(t)) {
                tickets.add(t);
            } else {
                System.out.println("Invalid ticket skipped: " + t);
            }
        }
    }

    // Display all available tickets
    public static void getAllTickets() {
        if (tickets.isEmpty()) {
            System.out.println("No available tickets at the moment.");
            return;
        }

        System.out.println("Available Tickets:");
        for (Ticket ticket : tickets) {
            System.out.println(ticket.toString());
        }
    }

    // Search for a ticket by ID
    public static Ticket getTicketInfo(int ticket_id) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicket_id() == ticket_id) {
                return ticket;
            }
        }
        return null; // No matching ticket found
    }

    // Assignment 2 requirement: validation logic
    private static boolean validateTicket(Ticket ticket) {
        if (ticket == null) return false;

        if (ticket.getTicket_id() <= 0) return false;
        if (ticket.getPassenger() == null) return false;
        if (ticket.getFlight() == null) return false;
        if (ticket.getPrice() < 0) return false;

        return true;
    }
}