package fit5171.monash.edu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    // A simple concrete subclass for testing abstract Person
    private static class DummyPerson extends Person {
        public DummyPerson() { super(); }
        public DummyPerson(String firstName, String secondName, int age, String gender) {
            super(firstName, secondName, age, gender);
        }
    }

    @Test
    void testValidConstructorAndGetters() {
        DummyPerson p = new DummyPerson("Alice", "Smith", 28, "Woman");
        assertEquals("Alice", p.getFirstName());
        assertEquals("Smith", p.getSecondName());
        assertEquals(28, p.getAge());
        assertEquals("Woman", p.getGender());
    }

    @Test
    void testDefaultConstructorAndSetters() {
        DummyPerson p = new DummyPerson();
        p.setFirstName("Bob");
        p.setSecondName("Jones");
        p.setAge(45);
        p.setGender("Man");

        assertEquals("Bob", p.getFirstName());
        assertEquals("Jones", p.getSecondName());
        assertEquals(45, p.getAge());
        assertEquals("Man", p.getGender());
    }

    @Test
    void testNameValidationInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson(null, "Last", 20, "Other"));
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson("", "Last", 20, "Other"));
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson("  ", "Last", 20, "Other"));
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson("1Invalid", "Last", 20, "Other"));
    }

    @Test
    void testSecondNameValidationInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson("First", null, 20, "Non-Binary"));
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson("First", "", 20, "Non-Binary"));
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson("First", "  ", 20, "Non-Binary"));
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson("First", "#Name", 20, "Non-Binary"));
    }

    @Test
    void testAgeValidationInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson("First", "Last", -1, "Other"));
    }

    @Test
    void testGenderValidationInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson("First", "Last", 30, null));
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson("First", "Last", 30, ""));
        assertThrows(IllegalArgumentException.class, () -> new DummyPerson("First", "Last", 30, "Unknown"));
    }

    @Test
    void testSetterValidations() {
        DummyPerson p = new DummyPerson();
        // First name
        assertThrows(IllegalArgumentException.class, () -> p.setFirstName(null));
        assertThrows(IllegalArgumentException.class, () -> p.setFirstName(""));
        assertThrows(IllegalArgumentException.class, () -> p.setFirstName("  "));
        assertThrows(IllegalArgumentException.class, () -> p.setFirstName("9Name"));

        // Second name
        assertThrows(IllegalArgumentException.class, () -> p.setSecondName(null));
        assertThrows(IllegalArgumentException.class, () -> p.setSecondName(""));
        assertThrows(IllegalArgumentException.class, () -> p.setSecondName("  "));
        assertThrows(IllegalArgumentException.class, () -> p.setSecondName("?Name"));

        // Age
        assertThrows(IllegalArgumentException.class, () -> p.setAge(-5));

        // Gender
        assertThrows(IllegalArgumentException.class, () -> p.setGender(null));
        assertThrows(IllegalArgumentException.class, () -> p.setGender(""));
        assertThrows(IllegalArgumentException.class, () -> p.setGender("InvalidGender"));
    }

    @Test
    void testAllowedGendersAreAccepted() {
        DummyPerson p = new DummyPerson();
        String[] genders = {"Woman", "Man", "Non-Binary", "Prefer not to say", "Other"};
        for (String g : genders) {
            p.setGender(g);
            assertEquals(g, p.getGender());
        }
    }

    @Test
    void testLowercaseNamesAccepted() {
        DummyPerson p = new DummyPerson();
        p.setFirstName("alice");
        p.setSecondName("smith");
        assertEquals("alice", p.getFirstName());
        assertEquals("smith", p.getSecondName());
    }

    @Test
    void testAgeZeroAccepted() {
        DummyPerson p = new DummyPerson();
        p.setAge(0);
        assertEquals(0, p.getAge());
    }

    @Test
    void testLeadingWhitespaceNameRejected() {
        DummyPerson p = new DummyPerson();
        assertThrows(IllegalArgumentException.class, () -> p.setFirstName(" Alice"));
        assertThrows(IllegalArgumentException.class, () -> p.setSecondName(" Bob"));
    }

    @Test
    void testTrailingWhitespaceNameAccepted() {
        DummyPerson p = new DummyPerson();
        p.setFirstName("Charlie ");
        assertEquals("Charlie ", p.getFirstName());
    }

    @Test
    void testToStringContainsFields() {
        DummyPerson p = new DummyPerson("Carol", "Danvers", 35, "Other");
        String str = p.toString();
        assertTrue(str.contains("Carol"));
        assertTrue(str.contains("Danvers"));
        assertTrue(str.contains("35"));
        assertTrue(str.contains("Other"));
    }
}
