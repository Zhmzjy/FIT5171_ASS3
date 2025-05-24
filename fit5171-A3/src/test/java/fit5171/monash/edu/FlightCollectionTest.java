package fit5171.monash.edu;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class FlightCollectionTest {

    private Airplane dummyPlane;
    private Flight good1, good2;

    @Before
    public void setUp() {
        // clear out any previously‐added flights
        FlightCollection.getFlights().clear();

        dummyPlane = new Airplane(1, "TestModel", 10, 20, 5);
        Timestamp t1 = Timestamp.valueOf("2025-01-01 08:00:00");
        Timestamp t2 = Timestamp.valueOf("2025-01-01 10:00:00");

        good1 = new Flight(101, "CityB", "CityA", "C101", "AirA", t1, t2, dummyPlane);
        good2 = new Flight(102, "CityC", "CityB", "C102", "AirB", t1, t2, dummyPlane);
    }

    //---- addFlight() validation ----

    @Test
    public void testAddFlight_success() {
        FlightCollection.addFlight(good1);
        assertEquals(1, FlightCollection.getFlights().size());
        assertSame(good1, FlightCollection.getFlights().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFlight_rejectsNull() {
        FlightCollection.addFlight(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFlight_rejectsZeroId() {
        Flight bad = new Flight(0, "CityX", "CityY", "C", "Co",
                Timestamp.valueOf("2025-01-01 00:00:00"),
                Timestamp.valueOf("2025-01-01 01:00:00"),
                dummyPlane);
        FlightCollection.addFlight(bad);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFlight_rejectsNegativeId() {
        Flight bad = new Flight(-5, "CityX", "CityY", "C", "Co",
                Timestamp.valueOf("2025-01-01 00:00:00"),
                Timestamp.valueOf("2025-01-01 01:00:00"),
                dummyPlane);
        FlightCollection.addFlight(bad);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFlight_rejectsInvalidFromCity() {
        Flight bad = new Flight(201, "CityY", "C1tyA!", "C", "Co",
                Timestamp.valueOf("2025-01-01 00:00:00"),
                Timestamp.valueOf("2025-01-01 01:00:00"),
                dummyPlane);
        FlightCollection.addFlight(bad);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFlight_rejectsInvalidToCity() {
        Flight bad = new Flight(202, "C!tyB", "CityA", "C", "Co",
                Timestamp.valueOf("2025-01-01 00:00:00"),
                Timestamp.valueOf("2025-01-01 01:00:00"),
                dummyPlane);
        FlightCollection.addFlight(bad);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFlight_rejectsNullCode() {
        Flight bad = new Flight(203, "CityB", "CityA", null, "Co",
                Timestamp.valueOf("2025-01-01 00:00:00"),
                Timestamp.valueOf("2025-01-01 01:00:00"),
                dummyPlane);
        FlightCollection.addFlight(bad);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFlight_rejectsEmptyCompany() {
        Flight bad = new Flight(204, "CityB", "CityA", "C", "",
                Timestamp.valueOf("2025-01-01 00:00:00"),
                Timestamp.valueOf("2025-01-01 01:00:00"),
                dummyPlane);
        FlightCollection.addFlight(bad);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFlight_rejectsNullDateFrom() {
        Flight bad = new Flight(205, "CityB", "CityA", "C", "Co",
                null,
                Timestamp.valueOf("2025-01-01 01:00:00"),
                dummyPlane);
        FlightCollection.addFlight(bad);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFlight_rejectsNullDateTo() {
        Flight bad = new Flight(206, "CityB", "CityA", "C", "Co",
                Timestamp.valueOf("2025-01-01 00:00:00"),
                null,
                dummyPlane);
        FlightCollection.addFlight(bad);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFlight_rejectsNullAirplane() {
        Flight bad = new Flight(207, "CityB", "CityA", "C", "Co",
                Timestamp.valueOf("2025-01-01 00:00:00"),
                Timestamp.valueOf("2025-01-01 01:00:00"),
                null);
        FlightCollection.addFlight(bad);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFlights_bulkRejectsOnInvalid() {
        ArrayList<Flight> list = new ArrayList<>();
        list.add(good1);
        // next one is invalid (empty departFrom)
        list.add(new Flight(208, "CityC", " ", "C", "Co",
                Timestamp.valueOf("2025-01-01 00:00:00"),
                Timestamp.valueOf("2025-01-01 01:00:00"),
                dummyPlane));
        FlightCollection.addFlights(list);
    }


    //---- getFlightInfo(city1,city2) ----

    @Test
    public void testGetFlightInfo_direct() {
        FlightCollection.addFlight(good1);
        Flight found = FlightCollection.getFlightInfo("CityA", "CityB");
        assertSame(good1, found);
    }

    @Test
    public void testGetFlightInfo_caseInsensitive() {
        FlightCollection.addFlight(good1);
        Flight found = FlightCollection.getFlightInfo("citya", "cityb");
        assertSame(good1, found);
    }

    @Test
    public void testGetFlightInfo_invalidCityName() {
        FlightCollection.addFlight(good1);
        // departFrom must match /^[A-Za-z ]+$/
        assertNull(FlightCollection.getFlightInfo("CityA!", "CityB"));
        assertNull(FlightCollection.getFlightInfo("CityA", "CityB1"));
    }

    @Test
    public void testGetFlightInfo_notFound() {
        assertNull(FlightCollection.getFlightInfo("X", "Y"));
    }


    //---- getFlightInfo(city) ----

    @Test
    public void testGetFlightInfo_arrivalOnly() {
        FlightCollection.addFlight(good2);
        Flight found = FlightCollection.getFlightInfo("CityC");
        assertSame(good2, found);
    }

    @Test
    public void testGetFlightInfo_arrivalInvalidCityName() {
        FlightCollection.addFlight(good2);
        assertNull(FlightCollection.getFlightInfo("CityC9"));
    }

    @Test
    public void testGetFlightInfo_arrivalNotFound() {
        assertNull(FlightCollection.getFlightInfo("Nowhere"));
    }


    //---- getFlightInfo(int) ----

    @Test
    public void testGetFlightInfo_byId() {
        FlightCollection.addFlight(good1);
        Flight found = FlightCollection.getFlightInfo(101);
        assertSame(good1, found);
    }

    @Test
    public void testGetFlightInfo_byId_notFound() {
        assertNull(FlightCollection.getFlightInfo(999));
    }
}

