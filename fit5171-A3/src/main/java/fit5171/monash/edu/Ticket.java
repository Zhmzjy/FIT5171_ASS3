package fit5171.monash.edu;

public class Ticket {
    private int ticket_id;
    private int price;
    Flight flight;
    private boolean classVip; // indicates if this is business class ticket or not
    private boolean status; // indicates status of ticket: if it is bought by someone or not
    Passenger passenger;

    // Added for A2 compliance
    private static final double SERVICE_TAX_PERCENT = 0.12;

    public Ticket(int ticket_id, int price, Flight flight, boolean classVip, Passenger passenger) {
        this.ticket_id = ticket_id;
        this.flight = flight;
        this.classVip = classVip;
        this.status = false;
        this.passenger = passenger;
        this.price = validateAndCalculatePrice(price, passenger);
    }

    public Ticket() {
        // Empty constructor
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        // Preserve existing method, but reroute logic to internal helper
        this.price = validateAndCalculatePrice(price, this.passenger);
    }

    public void saleByAge(int age) {
        // Original logic retained
        int price = getPrice();
        if (age < 15) {
            price -= (int) (price * 0.5); // 50% sale for children under 15
            this.price = price;
        } else if (age >= 60) {
            this.price = 0; // 100% sale for elder people
        }
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public boolean getClassVip() {
        return classVip;
    }

    public void setClassVip(boolean classVip) {
        this.classVip = classVip;
    }

    public boolean ticketStatus() {
        return status;
    }

    public void setTicketStatus(boolean status) {
        this.status = status;
    }

    public void serviceTax() {
        // Preserve original implementation
        this.price *= 1.12;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    @Override
    public String toString() {
        return "Ticket{" + '\n' +
                "Price=" + getPrice() + " KZT, " + '\n' +
                getFlight() + '\n' +
                "Vip status=" + getClassVip() + '\n' +
                getPassenger() + '\n' +
                "Ticket was purchased=" + ticketStatus() + "\n}";
    }



    private int validateAndCalculatePrice(int basePrice, Passenger passenger) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("Base price cannot be negative.");
        }

        double finalPrice = basePrice;

        if (passenger != null) {
            int age = passenger.getAge();
            if (age < 15) {
                finalPrice *= 0.5;
            } else if (age >= 60) {
                finalPrice = 0;
            }
        }

        // Apply service tax
        finalPrice += finalPrice * SERVICE_TAX_PERCENT;

        return (int) Math.round(finalPrice);
    }
}