package fit5171.monash.edu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FlightTest {

    @Mock
    private Airplane mockAirplane;

    @Before
    public void setUp() {
        // MockitoJUnitRunner will initialize @Mock fields for us
    }

    @Test
    public void testValidFlightCreation() {
        Timestamp from = Timestamp.valueOf("2025-05-01 10:00:00");
        Timestamp to   = Timestamp.valueOf("2025-05-01 12:00:00");

        Flight flight = new Flight(
                1, "CityB", "CityA", "FL123", "AirCo", from, to, mockAirplane
        );

        assertEquals(1,          flight.getFlightID());
        assertEquals("CityB",    flight.getDepartTo());
        assertEquals("CityA",    flight.getDepartFrom());
        assertEquals("FL123",    flight.getCode());
        assertEquals("AirCo",    flight.getCompany());
        assertSame  (mockAirplane, flight.getAirplane());
        assertEquals(from,       flight.getDateFrom());
        assertEquals(to,         flight.getDateTo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDepartToThrows() {
        new Flight(
                2, null, "CityA", "C1", "AirCo",
                Timestamp.valueOf("2025-05-01 10:00:00"),
                Timestamp.valueOf("2025-05-01 12:00:00"),
                mockAirplane
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDepartFromThrows() {
        new Flight(
                3, "CityB", "   ", "C2", "AirCo",
                Timestamp.valueOf("2025-05-01 10:00:00"),
                Timestamp.valueOf("2025-05-01 12:00:00"),
                mockAirplane
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCodeThrows() {
        new Flight(
                4, "CityB", "CityA", null, "AirCo",
                Timestamp.valueOf("2025-05-01 10:00:00"),
                Timestamp.valueOf("2025-05-01 12:00:00"),
                mockAirplane
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCompanyThrows() {
        new Flight(
                5, "CityB", "CityA", "C3", "",
                Timestamp.valueOf("2025-05-01 10:00:00"),
                Timestamp.valueOf("2025-05-01 12:00:00"),
                mockAirplane
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDateFromThrows() {
        new Flight(
                6, "CityB", "CityA", "C4", "AirCo",
                null,
                Timestamp.valueOf("2025-05-01 12:00:00"),
                mockAirplane
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDateToThrows() {
        new Flight(
                7, "CityB", "CityA", "C5", "AirCo",
                Timestamp.valueOf("2025-05-01 10:00:00"),
                null,
                mockAirplane
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAirplaneThrows() {
        new Flight(
                8, "CityB", "CityA", "C6", "AirCo",
                Timestamp.valueOf("2025-05-01 10:00:00"),
                Timestamp.valueOf("2025-05-01 12:00:00"),
                null
        );
    }

    @Test
    public void testDuplicateFlightThrows() {
        Timestamp from = Timestamp.valueOf("2025-06-01 08:00:00");
        Timestamp to   = Timestamp.valueOf("2025-06-01 10:00:00");

        // first creation OK
        new Flight(100, "A", "B", "DUPCODE", "AirCo", from, to, mockAirplane);

        // exactly same ID+code → IllegalArgumentException
        try {
            new Flight(100, "X", "Y", "DUPCODE", "OtherCo", from, to, mockAirplane);
            fail("Expected duplicate-flight exception");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testSameIdDifferentCodeAllowed() {
        Timestamp from = Timestamp.valueOf("2025-09-01 08:00:00");
        Timestamp to   = Timestamp.valueOf("2025-09-01 10:00:00");

        // first flight with CODEA
        new Flight(200, "Dst", "Src", "CODEA", "AirCo", from, to, mockAirplane);

        // same ID but different code → no exception
        new Flight(200, "Dst", "Src", "CODEB", "AirCo", from, to, mockAirplane);
    }

    @Test
    public void testToStringContainsFields() {
        Timestamp from = Timestamp.valueOf("2025-07-01 09:00:00");
        Timestamp to   = Timestamp.valueOf("2025-07-01 11:30:00");

        Flight flight = new Flight(
                300, "Dest", "Orig", "TS123", "TestCo", from, to, mockAirplane
        );

        String s = flight.toString();
        assertTrue(s.contains("Dest"));
        assertTrue(s.contains("Orig"));
        assertTrue(s.contains("TS123"));
        assertTrue(s.contains("TestCo"));
    }

    @Test
    public void testSettersAndGetters() {
        Timestamp from = Timestamp.valueOf("2025-08-01 07:00:00");
        Timestamp to   = Timestamp.valueOf("2025-08-01 09:00:00");

        Flight flight = new Flight(
                400, "To1", "From1", "C1", "Co1", from, to, mockAirplane
        );

        flight.setFlightID(401);
        assertEquals(401, flight.getFlightID());

        flight.setDepartTo("To2");
        assertEquals("To2", flight.getDepartTo());

        flight.setDepartFrom("From2");
        assertEquals("From2", flight.getDepartFrom());

        flight.setCode("C2");
        assertEquals("C2", flight.getCode());

        flight.setCompany("Co2");
        assertEquals("Co2", flight.getCompany());

        Timestamp newFrom = Timestamp.valueOf("2025-08-02 07:00:00");
        flight.setDateFrom(newFrom);
        assertEquals(newFrom, flight.getDateFrom());

        Timestamp newTo = Timestamp.valueOf("2025-08-02 09:00:00");
        flight.setDateTo(newTo);
        assertEquals(newTo, flight.getDateTo());

        // also show we can swap in another mock
        Airplane anotherMock = Mockito.mock(Airplane.class);
        flight.setAirplane(anotherMock);
        assertSame(anotherMock, flight.getAirplane());
    }
}
