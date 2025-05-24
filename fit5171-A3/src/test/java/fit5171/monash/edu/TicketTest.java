package fit5171.monash.edu;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TicketTest {


        @Mock private Flight mockFlight;
        @Mock private Passenger mockPassenger;

        @Before
        public void setUp() {
            // Initialize @Mock fields
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testConstructorCalculatesPrice_NoDiscountAdult() {
            // age 30 → no discount, just service tax
            when(mockPassenger.getAge()).thenReturn(30);
            Ticket t = new Ticket(1, 100, mockFlight, false, mockPassenger);

            assertEquals(1, t.getTicket_id());
            assertSame(mockFlight, t.getFlight());
            assertFalse(t.getClassVip());
            assertSame(mockPassenger, t.getPassenger());
            assertFalse(t.ticketStatus());

            // base=100, tax=12% → 112
            assertEquals(112, t.getPrice());
        }

        @Test(expected = IllegalArgumentException.class)
        public void testConstructor_NegativeBasePriceThrows() {
            new Ticket(2, -50, mockFlight, false, mockPassenger);
        }

        @Test
        public void testSetPrice_Recalculates() {
            when(mockPassenger.getAge()).thenReturn(30);
            Ticket t = new Ticket();       // empty constructor
            t.setPassenger(mockPassenger);
            t.setPrice(200);               // base=200, tax=24 →224

            assertEquals(224, t.getPrice());
        }

        @Test(expected = IllegalArgumentException.class)
        public void testSetPrice_NegativeThrows() {
            Ticket t = new Ticket();
            t.setPassenger(mockPassenger);
            t.setPrice(-1);
        }

        @Test
        public void testSaleByAge_ChildHalfPrice() {
            when(mockPassenger.getAge()).thenReturn(10);
            Ticket t = new Ticket(3, 100, mockFlight, false, mockPassenger);
            // initial price = 112
            assertEquals(112, t.getPrice());

            t.saleByAge(10);
            // half of 112
            assertEquals(56, t.getPrice());
        }

        @Test
        public void testSaleByAge_SeniorFree() {
            when(mockPassenger.getAge()).thenReturn(65);
            Ticket t = new Ticket(4, 100, mockFlight, false, mockPassenger);
            t.saleByAge(65);
            assertEquals(0, t.getPrice());
        }

        @Test
        public void testSaleByAge_AdultNoChange() {
            when(mockPassenger.getAge()).thenReturn(30);
            Ticket t = new Ticket(5, 100, mockFlight, false, mockPassenger);
            int p0 = t.getPrice();
            t.saleByAge(30);
            assertEquals(p0, t.getPrice());
        }

        @Test
        public void testServiceTax_AppliesAdditionalTax() {
            when(mockPassenger.getAge()).thenReturn(30);
            Ticket t = new Ticket(6, 100, mockFlight, false, mockPassenger);
            // initial = 112
            t.serviceTax();
            // 112 * 1.12 = 125.44 → rounded 125
            assertEquals((int)Math.round(112 * 1.12), t.getPrice());
        }

        @Test
        public void testGettersAndSettersBasic() {
            Ticket t = new Ticket();

            t.setTicket_id(99);
            assertEquals(99, t.getTicket_id());

            t.setFlight(mockFlight);
            assertSame(mockFlight, t.getFlight());

            t.setClassVip(true);
            assertTrue(t.getClassVip());

            t.setTicketStatus(true);
            assertTrue(t.ticketStatus());

            t.setPassenger(mockPassenger);
            assertSame(mockPassenger, t.getPassenger());
        }

//        @Test
//        public void testToString_IncludesAllRelevantParts() {
//            // stub toString for dependencies
//            when(mockFlight.toString()).thenReturn("Flight{ID=123}");
//            when(mockPassenger.toString()).thenReturn("Passenger{Jane Doe}");
//
//            when(mockPassenger.getAge()).thenReturn(30);
//            Ticket t = new Ticket(7, 50, mockFlight, true, mockPassenger);
//            // base=50→tax=6→56
//            String out = t.toString();
//
//            assertTrue(out.contains("Price=56"),   "Must show calculated price");
//            assertTrue(out.contains("Flight{ID=123}"));
//            assertTrue(out.contains("Vip status=true"));
//            assertTrue(out.contains("Passenger{Jane Doe}"));
//            assertTrue(out.contains("Ticket was purchased=false"));
//        }

    @Test
    public void testToString_IncludesAllRelevantParts() {
        when(mockFlight.toString()).thenReturn("Flight{ID=123}");
        when(mockPassenger.toString()).thenReturn("Passenger{Jane Doe}");
        when(mockPassenger.getAge()).thenReturn(30);

        Ticket t = new Ticket(7, 50, mockFlight, true, mockPassenger);
        String out = t.toString();

        assertTrue("Must show calculated price",            out.contains("Price=56"));
        assertTrue("Must include mocked flight toString",   out.contains("Flight{ID=123}"));
        assertTrue("Vip status must be shown",              out.contains("Vip status=true"));
        assertTrue("Passenger.toString() must appear",      out.contains("Passenger{Jane Doe}"));
        assertTrue("Ticket purchase status must appear",    out.contains("Ticket was purchased=false"));

        // negative checks
        assertFalse("Card number must not appear",          out.contains("9999999999999999"));
        assertFalse("CVV must not appear",                  out.contains("999"));
    }



}
