package org.nla;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class PersonTest {

    private Person person;

    @Before
    public void setup() {
        person = new Person("Nicolas", LocalDate.of(1983, 1, 17));
    }

    @Test
    public void testGetFirstName() throws Exception {
        assertThat(person.getFirstName(), equalTo("Nicolas"));
    }

    @Test
    public void testGetBirthDate() throws Exception {
        assertThat(person.getBirthDate(), equalTo(LocalDate.of(1983, 1, 17)));
    }

    @Test
    public void testGetAge() throws Exception {
        LocalDate now = LocalDate.of(2015, 1, 18);
        assertThat(person.getAge(now), equalTo(32));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(person.toString(), equalTo("Person{firstName='Nicolas', birthDate=1983-01-17}"));
    }
}