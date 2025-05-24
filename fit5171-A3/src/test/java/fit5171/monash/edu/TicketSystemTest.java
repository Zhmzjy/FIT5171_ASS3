package fit5171.monash.edu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TicketSystemTest {
    private final InputStream sysInBackup = System.in;

    @Before
    public void setUp() {
        // Clear out any flights or tickets left over from other tests
        FlightCollection.getFlights().clear();
        TicketCollection.getTickets().clear();
    }

    @After
    public void tearDown() {
        // Restore original System.in
        System.setIn(sysInBackup);
    }

    /**
     * Helper to feed newline‐separated console input.
     */
    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    public void testChooseTicket_DirectRoute_Success() {
        // --- Arrange ---
        Airplane airplane = new Airplane(10, "A320", 2, 3, 1);
        Timestamp from = Timestamp.valueOf("2025-05-01 10:00:00");
        Timestamp to   = Timestamp.valueOf("2025-05-01 12:00:00");
        Flight flight = new Flight(1, "CityB", "CityA", "FL1", "AirCo", from, to, airplane);
        FlightCollection.addFlight(flight);

        // Ticket price & passenger irrelevant for status/seat tests
        Passenger dummy = new Passenger(
                "X","Y",30,"Other",
                "x@y.com","0401 234 567","A12345678",
                "4111111111111111",123
        );
        Ticket ticket = new Ticket(101, 100, flight, false, dummy);
        TicketCollection.addTickets(
                new ArrayList<>(Arrays.asList(ticket))
        );

        // Simulate user input: firstName, secondName, age, gender, email, phone, passport, confirm(1), card, cvv
        String input =
                "Alice\n" +
                        "Smith\n" +
                        "30\n" +
                        "Man\n" +
                        "alice@example.com\n" +
                        "0401 234 567\n" +
                        "A12345678\n" +
                        "1\n" +               // purchase?
                        "4111111111111111\n" +
                        "123\n";
        provideInput(input);

        TicketSystem ts = new TicketSystem();

        // --- Act ---
        boolean result = ts.chooseTicket("CityA", "CityB");

        // --- Assert ---
        assertTrue("Direct purchase should succeed", result);

        Ticket bought = TicketCollection.getTicketInfo(101);
        assertTrue("Ticket status should now be booked", bought.ticketStatus());
        // economy seats should drop from 3 to 2
        assertEquals(2, airplane.getEconomySitsNumber());
    }

    @Test
    public void testChooseTicket_DirectRoute_CancelledByUser() {
        Airplane airplane = new Airplane(20, "B737", 1, 2, 1);
        Timestamp from = Timestamp.valueOf("2025-06-01 08:00:00");
        Timestamp to   = Timestamp.valueOf("2025-06-01 10:00:00");
        Flight flight = new Flight(2, "Dest", "Orig", "FL2", "AirCo", from, to, airplane);
        FlightCollection.addFlight(flight);

        Passenger dummy = new Passenger(
                "X","Y",30,"Other",
                "x@y.com","0401 234 567","A12345678",
                "4111111111111111",123
        );
        Ticket t = new Ticket(102, 100, flight, false, dummy);
        TicketCollection.addTickets(
                new ArrayList<>(Arrays.asList(t))
        );

        // User enters data then "0" at purchase prompt
        String input =
                "Bob\n" +
                        "Jones\n" +
                        "40\n" +
                        "Man\n" +
                        "bob@ex.com\n" +
                        "0402 345 678\n" +
                        "B23456789\n" +
                        "0\n"; // cancel
        provideInput(input);

        TicketSystem ts = new TicketSystem();
        boolean result = ts.chooseTicket("Orig", "Dest");

        assertFalse("User cancelled purchase should return false", result);
        assertFalse("Ticket remains unbooked", t.ticketStatus());
        // economy seats unchanged
        assertEquals(2, airplane.getEconomySitsNumber());
    }

    @Test
    public void testChooseTicket_InvalidCityNames() {
        TicketSystem ts = new TicketSystem();
        // numbers not allowed
        assertFalse(ts.chooseTicket("City1", "City2"));
        // null or blank
        assertFalse(ts.chooseTicket("", "CityB"));
        assertFalse(ts.chooseTicket("CityA", null));
    }

    @Test
    public void testChooseTicket_NoDirect_NoTransfer() {
        // Empty collections → no flights at all
        TicketSystem ts = new TicketSystem();
        assertFalse("With no flights, chooseTicket should return false",
                ts.chooseTicket("A", "B"));
    }

    @Test
    public void testChooseTicket_TransferRoute_Success() {
        // Setup two‐leg route: A->C and C->B
        Airplane plane1 = new Airplane(30, "CRJ", 1, 2, 1);
        Airplane plane2 = new Airplane(31, "E190", 1, 2, 1);
        Timestamp f1 = Timestamp.valueOf("2025-07-01 06:00:00");
        Timestamp t1 = Timestamp.valueOf("2025-07-01 08:00:00");
        Timestamp f2 = Timestamp.valueOf("2025-07-01 09:00:00");
        Timestamp t2 = Timestamp.valueOf("2025-07-01 11:00:00");

        Flight leg1 = new Flight(10, "CityC", "CityA", "L1", "Air1", f1, t1, plane1);
        Flight leg2 = new Flight(11, "CityB", "CityC", "L2", "Air2", f2, t2, plane2);
        FlightCollection.addFlights(
                new ArrayList<>(Arrays.asList(leg1, leg2))
        );

        Passenger dummy = new Passenger(
                "X","Y",30,"Other",
                "x@y.com","0401 234 567","A12345678",
                "4111111111111111",123
        );
        Ticket t1obj = new Ticket(201, 200, leg1, false, dummy);
        Ticket t2obj = new Ticket(202, 300, leg2, false, dummy);
        TicketCollection.addTickets(
                new ArrayList<>(Arrays.asList(t1obj, t2obj))
        );

        // Simulate user input for one round of passenger data + confirm + payment
        String input =
                "Charlie\n" +
                        "Delta\n" +
                        "25\n" +
                        "Man\n" +
                        "cd@ex.com\n" +
                        "0403 456 789\n" +
                        "C34567890\n" +
                        "1\n" +              // purchase?
                        "4111111111111111\n" +
                        "456\n";
        provideInput(input);

        TicketSystem ts = new TicketSystem();
        boolean result = ts.chooseTicket("CityA", "CityB");

        assertTrue("Transfer purchase should succeed", result);
        assertTrue(t1obj.ticketStatus());
        assertTrue(t2obj.ticketStatus());
        // seats decremented on both aircraft
        assertEquals(1, plane1.getEconomySitsNumber());
        assertEquals(1, plane2.getEconomySitsNumber());
    }
}
