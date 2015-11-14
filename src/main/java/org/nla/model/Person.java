package org.nla.model;

import java.time.LocalDate;
import java.time.Period;

public class Person implements Comparable<Person> {

    public enum Gender {
        male,
        female
    }

    private String firstName;

    private LocalDate birthDate;

    private Gender gender;

    public Person(String firstName, LocalDate birthDate, Gender gender) {
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
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
    public int compareTo(Person p) {
        return Integer.compare(p.getAge(LocalDate.now()), getAge(LocalDate.now()));
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", birthDate=" + birthDate +
                ", gender=" + gender +
                '}';
    }
}
