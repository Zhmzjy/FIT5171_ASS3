package fit5171.monash.edu;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TicketCollectionTest {

    private Passenger p;
    private Flight f;
    private Ticket t1, t2;

    @Before
    public void setUp() {
        // clear out any previously‐added tickets
        TicketCollection.getTickets().clear();

        // build a valid Passenger
        p = new Passenger(
                "John", "Doe", 30, "Man",
                "john@example.com",
                "0412 345 678",
                "A1234567",
                "1234567812345678",
                123
        );

        // build a valid Flight (with a dummy Airplane)
        Airplane plane = new Airplane(1, "TestModel", 5, 50, 5);
        Timestamp dep = Timestamp.valueOf("2025-01-01 10:00:00");
        Timestamp arr = Timestamp.valueOf("2025-01-01 12:00:00");
        f = new Flight( 1, "CityA", "CityB", "C1", "Co", dep, arr, plane );

        // two tickets: one economy, one VIP
        t1 = new Ticket( 1, 100, f, false, p );
        t2 = new Ticket( 2, 200, f, true,  p );
    }

    @Test
    public void testGetTicketsInitiallyEmpty() {
        assertNotNull("List must exist",    TicketCollection.getTickets());
        assertTrue("Should start empty",    TicketCollection.getTickets().isEmpty());
        assertFalse("Size>0 should be false", TicketCollection.getTickets().size() > 0);
    }

    @Test
    public void testAddTicketsAndRetrieve() {
        ArrayList<Ticket> list = new ArrayList<>(Arrays.asList(t1, t2));
        TicketCollection.addTickets(list);

        assertEquals("Two tickets added",   2, TicketCollection.getTickets().size());
        assertSame("ID 1 lookup",            t1, TicketCollection.getTicketInfo(1));
        assertSame("ID 2 lookup",            t2, TicketCollection.getTicketInfo(2));
    }

    @Test(expected = NullPointerException.class)
    public void testAddTicketsNullThrows() {
        // passing null should cause an NPE during iteration
        TicketCollection.addTickets(null);
    }

    @Test
    public void testGetTicketInfoNotFound() {
        // add one, then lookup a different ID
        TicketCollection.addTickets(new ArrayList<>(Arrays.asList(t1)));
        assertNull("No ticket with ID 999", TicketCollection.getTicketInfo(999));
    }

    @Test
    public void testGetAllTicketsEmpty() {
        // capture System.out
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        TicketCollection.getAllTickets();
        String printed = out.toString();

        assertTrue("Should mention no tickets", printed.contains("No available tickets"));
    }

    @Test
    public void testGetAllTicketsNonEmpty() {
        // capture System.out
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        TicketCollection.addTickets(new ArrayList<>(Arrays.asList(t1)));
        TicketCollection.getAllTickets();
        String printed = out.toString();

        assertTrue("Header must appear",          printed.contains("Available Tickets"));
        assertTrue("Ticket.toString must appear", printed.contains(t1.toString()));
    }
}
