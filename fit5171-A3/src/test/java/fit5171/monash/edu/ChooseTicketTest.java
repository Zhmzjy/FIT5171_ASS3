package fit5171.monash.edu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ChooseTicketTest {
    private final InputStream  sysInBackup  = System.in;
    private final PrintStream sysOutBackup = System.out;
    private ByteArrayOutputStream testOut;

    @Before
    public void setUp() {
        // Clear any shared state
        FlightCollection.getFlights().clear();
        TicketCollection.getTickets().clear();

        // Capture stdout
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void tearDown() {
        // Restore stdin/stdout
        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);
    }

    /** Helper to feed newline‐separated console input. */
    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    public void testDirectPurchaseIntegration() {
        // --- Arrange ---
        Airplane plane = new Airplane(1, "A320", 2, 3, 1);
        Timestamp from = Timestamp.valueOf("2025-05-01 10:00:00");
        Timestamp to   = Timestamp.valueOf("2025-05-01 12:00:00");
        Flight flight = new Flight(100, "B", "A", "FL100", "AirCo", from, to, plane);
        FlightCollection.addFlights(new ArrayList<>(Arrays.asList(flight)));

        Passenger passenger = new Passenger(
                "John","Doe",30,"Man",
                "john@example.com",
                "0401 234 567",
                "A12345678",
                "4111111111111111",
                123
        );
        Ticket ticket = new Ticket(500, 100, flight, false, passenger);
        TicketCollection.addTickets(new ArrayList<>(Arrays.asList(ticket)));

        String input =
                "A\n" +
                        "B\n" +
                        "500\n" +
                        "Alice\n" +
                        "Smith\n" +
                        "28\n" +
                        "Woman\n" +
                        "alice@domain.com\n" +
                        "0402 345 678\n" +
                        "B98765432\n" +
                        "1\n" +
                        "4222222222222\n" +
                        "321\n";
        provideInput(input);

        ChooseTicket chooser = new ChooseTicket();

        // --- Act ---
        chooser.chooseTicket();

        // --- Assert ---
        Ticket booked = TicketCollection.getTicketInfo(500);
        assertTrue("Ticket should be booked", booked.ticketStatus());
        assertEquals("Economy seats decremented", 2, plane.getEconomySitsNumber());

        String out = testOut.toString();
        assertTrue(out.contains("Direct flight available."));
        assertTrue(out.contains("Enter ticket ID to purchase:"));
    }

    @Test
    public void testTransferPurchaseIntegration() {
        // --- Arrange ---
        Airplane p1 = new Airplane(2, "CRJ", 1, 2, 1);
        Airplane p2 = new Airplane(3, "E190",1, 2, 1);
        Timestamp f1 = Timestamp.valueOf("2025-06-01 06:00:00");
        Timestamp t1 = Timestamp.valueOf("2025-06-01 08:00:00");
        Timestamp f2 = Timestamp.valueOf("2025-06-01 09:00:00");
        Timestamp t2 = Timestamp.valueOf("2025-06-01 11:00:00");

        Flight leg1 = new Flight(200, "C", "A", "L200", "Air1", f1, t1, p1);
        Flight leg2 = new Flight(201, "B", "C", "L201", "Air2", f2, t2, p2);
        FlightCollection.addFlights(new ArrayList<>(Arrays.asList(leg1, leg2)));

        Passenger passenger = new Passenger(
                "X","Y",30,"Other",
                "x@y.com","0401 234 567",
                "A11122222","4111...",123
        );
        Ticket tA = new Ticket(600, 150, leg1, false, passenger);
        Ticket tB = new Ticket(601, 200, leg2, false, passenger);
        TicketCollection.addTickets(new ArrayList<>(Arrays.asList(tA, tB)));

        String input =
                "A\n" +
                        "B\n" +
                        "600\n" +
                        "601\n" +
                        "Eve\n" +
                        "Adams\n" +
                        "22\n" +
                        "Woman\n" +
                        "eve@ex.com\n" +
                        "0403 456 789\n" +
                        "C12345678\n" +
                        "1\n" +
                        "4333333333333333\n" +
                        "555\n";
        provideInput(input);

        ChooseTicket chooser = new ChooseTicket();

        // --- Act ---
        chooser.chooseTicket();

        // --- Assert ---
        assertTrue("First leg booked",  tA.ticketStatus());
        assertTrue("Second leg booked", tB.ticketStatus());
        assertEquals("Leg1 economy--", 1, p1.getEconomySitsNumber());
        assertEquals("Leg2 economy--", 1, p2.getEconomySitsNumber());

        String out = testOut.toString();
        assertTrue(out.contains("Transfer route found: A -> C -> B"));
        assertTrue(out.contains("Enter ticket ID for first leg:"));
    }

    @Test
    public void testDirectPurchaseCancelledByUser() {
        // --- Arrange ---
        Airplane plane = new Airplane(4, "B737",1,2,1);
        Timestamp f = Timestamp.valueOf("2025-07-01 07:00:00");
        Timestamp to= Timestamp.valueOf("2025-07-01 09:00:00");
        Flight flight = new Flight(300, "B","A","FL300","AirX",f,to,plane);
        FlightCollection.addFlights(new ArrayList<>(Arrays.asList(flight)));

        Passenger passenger = new Passenger(
                "U","N",40,"Man",
                "u@n.com","0404 567 890",
                "D87654321","4111...",321
        );
        Ticket ticket = new Ticket(700,120,flight,false,passenger);
        TicketCollection.addTickets(new ArrayList<>(Arrays.asList(ticket)));

        String input =
                "A\n" +
                        "B\n" +
                        "700\n" +
                        "Bob\n" +
                        "Brown\n" +
                        "40\n" +
                        "Man\n" +
                        "bob@b.com\n" +
                        "0405 678 901\n" +
                        "E12345678\n" +
                        "0\n";
        provideInput(input);

        ChooseTicket chooser = new ChooseTicket();

        // --- Act ---
        chooser.chooseTicket();

        // --- Assert ---
        assertFalse("Ticket must remain unbooked", ticket.ticketStatus());
        assertEquals("Economy unchanged", 2, plane.getEconomySitsNumber());

        String out = testOut.toString();
        assertTrue(out.contains("Direct flight available."));
    }
}
