package fit5171.monash.edu;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AirplaneTest {

    @Test
    void testValidAirplaneCreation() {
        Airplane airplane = new Airplane(1, "Boeing 737", 10, 50, 5);
        assertEquals(1, airplane.getAirplaneID());
        assertEquals("Boeing 737", airplane.getAirplaneModel());
        assertEquals(10, airplane.getBusinessSitsNumber());
        assertEquals(50, airplane.getEconomySitsNumber());
        assertEquals(5, airplane.getCrewSitsNumber());

        Map<String, Boolean> seatMap = airplane.getSeatMap();
        assertNotNull(seatMap, "Seat map should be initialized");
        assertEquals(10 * 7, seatMap.size(), "There should be 70 seats (rows A-J, 7 each)");

        for (char row = 'A'; row <= 'J'; row++) {
            for (int col = 1; col <= 7; col++) {
                String seat = row + String.valueOf(col);
                assertTrue(seatMap.containsKey(seat), "Missing seat: " + seat);
                assertFalse(seatMap.get(seat), "Seat should be unoccupied initially: " + seat);
            }
        }
    }

    @Test
    void testAirplaneIDValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> new Airplane(0, "Model", 1, 1, 1),
                "Airplane ID must be positive");
        assertThrows(IllegalArgumentException.class,
                () -> new Airplane(-10, "Model", 1, 1, 1),
                "Airplane ID must be positive");
    }

    @Test
    void testModelValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> new Airplane(1, null, 1, 1, 1),
                "Airplane model cannot be null or empty");
        assertThrows(IllegalArgumentException.class,
                () -> new Airplane(1, "   ", 1, 1, 1),
                "Airplane model cannot be null or empty");
    }

    @Test
    void testSeatCountValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> new Airplane(1, "Model", -1, 5, 2),
                "Business seat count cannot be negative");
        assertThrows(IllegalArgumentException.class,
                () -> new Airplane(1, "Model", 2, -5, 2),
                "Economy seat count cannot be negative");
        assertThrows(IllegalArgumentException.class,
                () -> new Airplane(1, "Model", 2, 5, -3),
                "Crew seat count cannot be negative");
    }

    @Test
    void testZeroSeatAllowed() {
        Airplane airplane = new Airplane(2, "TinyPlane", 0, 0, 0);
        assertEquals(0, airplane.getBusinessSitsNumber());
        assertEquals(0, airplane.getEconomySitsNumber());
        assertEquals(0, airplane.getCrewSitsNumber());
        assertEquals(70, airplane.getSeatMap().size(),
                "Seat map size should remain 70 regardless of seat counts");
    }

    @Test
    void testSettersAndGetters() {
        Airplane airplane = new Airplane(1, "Initial", 5, 10, 2);

        airplane.setAirplaneID(2);
        assertEquals(2, airplane.getAirplaneID());

        airplane.setAirplaneModel("UpdatedModel");
        assertEquals("UpdatedModel", airplane.getAirplaneModel());

        airplane.setBusinessSitsNumber(3);
        assertEquals(3, airplane.getBusinessSitsNumber());

        airplane.setEconomySitsNumber(4);
        assertEquals(4, airplane.getEconomySitsNumber());

        airplane.setCrewSitsNumber(1);
        assertEquals(1, airplane.getCrewSitsNumber());
    }

    @Test
    void testSetterValidation() {
        Airplane airplane = new Airplane(1, "Model", 1, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneID(0));
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneID(-5));

        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneModel(null));
        assertThrows(IllegalArgumentException.class, () -> airplane.setAirplaneModel("  "));

        assertThrows(IllegalArgumentException.class, () -> airplane.setBusinessSitsNumber(-1));
        assertThrows(IllegalArgumentException.class, () -> airplane.setEconomySitsNumber(-2));
        assertThrows(IllegalArgumentException.class, () -> airplane.setCrewSitsNumber(-3));
    }

    @Test
    void testToStringContainsFields() {
        Airplane airplane = new Airplane(1, "ModelX", 5, 15, 3);
        String str = airplane.toString();
        assertTrue(str.contains("ModelX"), "toString should include the model");
        assertTrue(str.contains("business sits=5"), "toString should include business sits");
        assertTrue(str.contains("economy sits=15"), "toString should include economy sits");
        assertTrue(str.contains("crew sits=3"), "toString should include crew sits");
    }

    @Test
    void testSeatMapMutability() {
        Airplane airplane = new Airplane(1, "M", 0, 0, 0);
        Map<String, Boolean> seatMap = airplane.getSeatMap();
        assertFalse(seatMap.get("C3"), "Seat should initially be unoccupied");
        seatMap.put("C3", true);
        assertTrue(seatMap.get("C3"), "Seat mutability should allow marking occupied");
        assertFalse(seatMap.get("A1"), "Other seats should remain unchanged");
    }

    @Test
    void testGetAirPlaneInfoStub() {
        assertNull(Airplane.getAirPlaneInfo(999),
                "getAirPlaneInfo should return null until implemented");
    }
}
