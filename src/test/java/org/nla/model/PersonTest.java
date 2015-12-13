package org.nla.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class PersonTest {

    private Person person;

    @Before
    public void setup() {
        person = new Person("Nicolas", LocalDate.of(1983, 1, 17), Person.Gender.male);
    }

    @Test
    public void testGetFirstName() throws Exception {
        assertThat(person.getFirstName()).isEqualTo("Nicolas");
    }

    @Test
    public void testGetBirthDate() throws Exception {
        assertThat(person.getBirthDate()).isEqualTo(LocalDate.of(1983, 1, 17));
    }

    @Test
    public void testGetAge() throws Exception {
        LocalDate now = LocalDate.of(2015, 1, 18);
        assertThat(person.getAge(now)).isEqualTo(32);
    }

    @Test
    public void testToString() throws Exception {
        assertThat(person.toString()).isEqualTo("Person{firstName='Nicolas', birthDate=1983-01-17, gender=male}");
    }
}