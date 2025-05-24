package fit5171.monash.edu;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
//person classs
public abstract class Person {
    private String firstName;
    private String secondName;
    private int age;
    private String gender;

    private static final Set<String> ALLOWED_GENDERS = new HashSet<>(Arrays.asList(
            "Woman", "Man", "Non-Binary", "Prefer not to say", "Other"
    ));

    public Person() {}

    public Person(String firstName, String secondName, int age, String gender) {
        setFirstName(firstName);
        setSecondName(secondName);
        setAge(age);
        setGender(gender);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative.");
        }
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (gender == null || !ALLOWED_GENDERS.contains(gender)) {
            throw new IllegalArgumentException("Invalid gender: " + gender);
        }
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (!isValidName(firstName)) {
            throw new IllegalArgumentException("First name must start with a letter and cannot be null or empty.");
        }
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        if (!isValidName(secondName)) {
            throw new IllegalArgumentException("Second name must start with a letter and cannot be null or empty.");
        }
        this.secondName = secondName;
    }

    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && Character.isLetter(name.charAt(0));
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}