package fit5171.monash.edu;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PassangerTest {

        private static final String FIRST      = "Jane";
        private static final String LAST       = "Doe";
        private static final int    AGE        = 30;
        private static final String GENDER     = "Woman";
        private static final String EMAIL      = "jane.doe@example.com";
        private static final String AUS_LOCAL  = "0412 345 678";
        private static final String AUS_INTL   = "+61 412 345 678";
        private static final String US_LOCAL   = "123-456-7890";
        private static final String US_INTL    = "+1 123 456 7890";
        private static final String IND_LOCAL  = "01234567890";
        private static final String IND_INTL   = "+91 12345 67890";
        private static final String AUS_PASS   = "A1234567"; // Australia
        private static final String US_PASS    = "123456789"; // USA
        private static final String IND_PASS   = "B1234567"; // India
        private static final String CARD       = "4111111111111111";
        private static final int    SCODE      = 321;

        /**
         * 1. All fields mandatory + happy‐path constructor + getters
         */
        @Test
        public void testValidConstructorAndGetters() {
            // spy the Person superclass so we don't accidentally hit any Person logic
            Passenger p = spy(new Passenger(FIRST, LAST, AGE, GENDER,
                    EMAIL, AUS_LOCAL, AUS_PASS,
                    CARD, SCODE));
            // verify Person setters were called
            verify(p).setFirstName(FIRST);
            verify(p).setSecondName(LAST);
            verify(p).setAge(AGE);
            verify(p).setGender(GENDER);

            // now verify our own fields
            assertEquals(EMAIL, p.getEmail());
            assertEquals(AUS_LOCAL, p.getPhoneNumber());
            assertEquals(AUS_PASS, p.getPassport());
            assertEquals(CARD, p.getCardNumber());
            assertEquals(SCODE, p.getSecurityCode());
        }

        /**
         * 2. Email must match abc@domain.com
         */
        @Test(expected = IllegalArgumentException.class)
        public void testConstructorInvalidEmail() {
            new Passenger(FIRST, LAST, AGE, GENDER,
                    "bad-email", AUS_LOCAL, AUS_PASS,
                    CARD, SCODE);
        }

        @Test
        public void testSetEmailValidAndInvalid() {
            Passenger p = new Passenger();
            p.setEmail(EMAIL);
            assertEquals(EMAIL, p.getEmail());

            try {
                p.setEmail("no-at-symbol.com");
                fail("Expected exception for invalid email");
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        /**
         * 3. Phone must support AUS local & +61, plus two other countries (US and India here).
         */
        @Test
        public void testSetPhoneAllValidFormats() {
            Passenger p = new Passenger();
            // AUS
            p.setPhoneNumber(AUS_LOCAL);
            p.setPhoneNumber(AUS_INTL);
            // US
            p.setPhoneNumber(US_LOCAL);
            p.setPhoneNumber(US_INTL);
            // India
            p.setPhoneNumber(IND_LOCAL);
            p.setPhoneNumber(IND_INTL);
        }

        @Test
        public void testSetPhoneInvalid() {
            Passenger p = new Passenger();
            try {
                p.setPhoneNumber("000");
                fail("Expected exception for invalid phone");
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        /**
         * 4. Passport format for AUS, USA, India
         */
        @Test
        public void testSetPassportValidAndInvalid() {
            Passenger p = new Passenger();
            p.setPassport(AUS_PASS);
            p.setPassport(US_PASS);
            p.setPassport(IND_PASS);

            try {
                p.setPassport("WRONG123");
                fail("Expected exception for invalid passport");
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void testConstructorInvalidPassport() {
            new Passenger(FIRST, LAST, AGE, GENDER,
                    EMAIL, AUS_LOCAL, "BADPPT",
                    CARD, SCODE);
        }

        /**
         * 1 (again). Null or missing mandatory fields throw
         */
        @Test(expected = IllegalArgumentException.class)
        public void testConstructorNullMandatory() {
            new Passenger(null, LAST, AGE, GENDER,
                    EMAIL, AUS_LOCAL, AUS_PASS,
                    CARD, SCODE);
        }



}
