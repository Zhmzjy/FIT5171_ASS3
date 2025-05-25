package fit5171.monash.edu;

import org.junit.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BuyTicketTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;

    private Airplane airplane;
    private Flight flight;
    private Ticket ticket1;
    private Ticket ticket2;

    @Before
    public void setUp() {
        // Capture stdout
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Clear collections
        FlightCollection.getFlights().clear();
        TicketCollection.getTickets().clear();

        // Create an Airplane with 5 economy seats
        airplane = new Airplane(1, "TestPlane", 2, 5, 2);

        // Create a Flight and register it
        Timestamp depart  = Timestamp.valueOf("2025-01-01 10:00:00");
        Timestamp arrive  = Timestamp.valueOf("2025-01-01 12:00:00");
        int flightId = (int) (System.currentTimeMillis() % 100000);
        flight = new Flight(flightId, "CityA", "CityB", "F" + flightId, "Comp", depart, arrive, airplane);
        FlightCollection.addFlight(flight);

        // Create two economy tickets (basePrice=100 → final=112 KZT)
        ticket1 = new Ticket(1, 100, flight, false, null);
        ticket2 = new Ticket(2, 100, flight, false, null);
        TicketCollection.addTickets(new ArrayList<>(Arrays.asList(ticket1, ticket2)));
    }

    @After
    public void tearDown() {
        // Restore stdout
        System.setOut(originalOut);
    }

    @Test
    public void testBuyTicketSingle_SuccessFlow() {
        Ticket ticket = new Ticket(101, 200, flight, false, null);
        TicketCollection.addTickets(new ArrayList<>(Arrays.asList(ticket)));

        BuyTicket service = new BuyTicket();
        outContent.reset();
        service.buyTicket(101);

        assertTrue("Ticket should be purchased", ticket.ticketStatus());

        // 2) Economy seat count decremented by exactly 1
        assertEquals("Economy seats should drop by 1", 4, airplane.getEconomySitsNumber());

        // 3) Price was reset to 200 → serviceTax(12%) → 224
        assertEquals("Price should be 224", 224, ticket.getPrice());

        // 4) Passenger details filled in
        Passenger p = ticket.getPassenger();
        assertEquals("First name set", "John", p.getFirstName());
        assertEquals("Last name set",  "Doe",  p.getSecondName());
        assertEquals("Card number set",
                "1234567890123456", p.getCardNumber());
        assertEquals("Security code set", 123, p.getSecurityCode());

        // 5) Console output includes the bill
        String output = outContent.toString();
        assertTrue("Should show total bill",
                output.contains("Total bill: 224"));
    }

    @Test
    public void testBuyTicketSingle_InvalidId() {
        BuyTicket service = new BuyTicket();
        service.buyTicket(999);  // no such ticket

        // Must not throw, but print error message
        String output = outContent.toString();
        assertTrue("Error printed for missing ticket",
                output.contains("This ticket does not exist."));
    }

    @Test
    public void testBuyTicketTwoLegs_SuccessFlow() {
        BuyTicket service = new BuyTicket();
        service.buyTicket(1, 2);

        // Both tickets purchased
        assertTrue("Ticket1 purchased", ticket1.ticketStatus());
        assertTrue("Ticket2 purchased", ticket2.ticketStatus());

        // Economy seats decremented twice
        assertEquals("Economy seats should drop by 2", 3,
                airplane.getEconomySitsNumber());

        // Both tickets had base=100→112.0 each → sum=224.0
        String output = outContent.toString();
        assertTrue("Should show total bill for two legs",
                output.contains("Total bill: 224.0"));
    }

    @Test
    public void testBuyTicketTwoLegs_OneInvalid() {
        BuyTicket service = new BuyTicket();
        service.buyTicket(1, 999);  // second ticket missing

        String output = outContent.toString();
        assertTrue("Error printed for invalid second leg",
                output.contains("One or both tickets are invalid."));
    }
}