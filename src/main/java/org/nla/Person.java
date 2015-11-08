package org.nla;

import java.time.LocalDate;
import java.time.Period;

public class Person {

    private String firstName;

    private LocalDate birthDate;

    public Person(String firstName, LocalDate birthDate) {
        this.firstName = firstName;
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getAge(LocalDate atDate) {
        Period period = Period.between(birthDate, atDate);
        return period.getYears();
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
