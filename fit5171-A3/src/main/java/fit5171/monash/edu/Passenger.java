package fit5171.monash.edu;

import java.util.regex.Pattern;

public class Passenger extends Person {
    private String email;
    private String phoneNumber;
    private String cardNumber;
    private int securityCode;
    private String passport;

    public Passenger() {}

    public Passenger(String firstName, String secondName, int age, String gender,
                     String email, String phoneNumber, String passport,
                     String cardNumber, int securityCode) {
        super.setFirstName(firstName);
        super.setSecondName(secondName);
        super.setAge(age);
        super.setGender(gender);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setPassport(passport);
        this.cardNumber = cardNumber;
        this.securityCode = securityCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email;
    }

    public String getFirstName() {
        return super.getFirstName();
    }

    public String getSecondName() {
        return super.getSecondName();
    }

    public void setSecondName(String secondName) {
        super.setSecondName(secondName);
    }

    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    public String getPassport() {
        return passport;
    }

    public void setGender(String gender) {
        super.setGender(gender);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
        this.phoneNumber = phoneNumber;
    }

    private boolean isValidPhoneNumber(String number) {
        return Pattern.matches("^04\\d{2} \\d{3} \\d{3}$", number) ||                          // Australia local
                Pattern.matches("^\\+61 4\\d{2} \\d{3} \\d{3}$", number) ||                    // Australia intl
                Pattern.matches("^\\+1 \\d{3} \\d{3} \\d{4}$", number) ||                      // US intl
                Pattern.matches("^\\d{3}-\\d{3}-\\d{4}$", number) ||                           // US local
                Pattern.matches("^\\+91 \\d{5} \\d{5}$", number) ||                            // India intl
                Pattern.matches("^0\\d{10}$", number);                                         // India local
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
    }

    public void setPassport(String passport) {
        if (!isValidPassport(passport)) {
            throw new IllegalArgumentException("Invalid passport format.");
        }
        this.passport = passport;
    }

    private boolean isValidPassport(String passport) {
        return Pattern.matches("^[A-Z][0-9]{7,8}$", passport) ||        // Australia
                Pattern.matches("^\\d{9}$", passport) ||                 // USA
                Pattern.matches("^[A-Z]{1}[0-9]{7}$", passport);         // India
    }

    @Override
    public String getGender() {
        return super.getGender();
    }

    @Override
    public int getAge() {
        return super.getAge();
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "Fullname=" + super.getFirstName() + " " + super.getSecondName() +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", passport='" + passport + '\'' +
                '}';
    }


}
