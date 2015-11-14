package org.nla.java;

import org.junit.Before;
import org.junit.Test;
import org.nla.model.Person;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

// page à checker http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/

public class PersonStreamTest {

    private List<Person> persons;

    @Before
    public void setup() {
        persons = Arrays.asList(
                new Person("Nico", LocalDate.of(1983, 2, 23), Person.Gender.male),
                new Person("Sophie", LocalDate.of(1982, 1, 14), Person.Gender.female),
                new Person("Steph", LocalDate.of(1973, 7, 18), Person.Gender.female),
                new Person("Steph", LocalDate.of(1953, 9, 28), Person.Gender.male),
                new Person("Basile", LocalDate.of(1978, 7, 3), Person.Gender.male),
                new Person("John", LocalDate.of(1992, 11, 5), Person.Gender.male)
        );
    }

    @Test
    public void countWomen() {
        long femaleCount = persons
                .stream()
                .filter(p -> p.getGender() == Person.Gender.female)
                .count();

        assertThat(femaleCount).isEqualTo(2L);
    }

    @Test
    public void orderPersonsByNaturalOrder() {
        String joined = persons
                .stream()
                .sorted()
                .map(p -> p.getFirstName())
                .collect(Collectors.joining(", "));

        assertThat(joined).isEqualTo("Steph, Steph, Basile, Sophie, Nico, John");
    }

    @Test
    public void orderPersonsByFirstName() {
        List<String> joined = persons
                .stream()
                .sorted((p1, p2) -> p1.getFirstName().compareTo(p2.getFirstName()))
                .map(p -> p.getFirstName())
                .collect(Collectors.toList());

        assertThat(joined).containsSequence("Basile", "John", "Nico", "Sophie", "Steph", "Steph");
    }

    @Test
    public void orderPersonsByFirstNameAndSelectDistinct() {
        List<String> joined = persons
                .stream()
                .sorted((p1, p2) -> p1.getFirstName().compareTo(p2.getFirstName()))
                .map(p -> p.getFirstName())
                .distinct()
                .collect(Collectors.toList());

        assertThat(joined).containsSequence("Basile", "John", "Nico", "Sophie", "Steph");
    }

    @Test
    public void orderPersonsByFirstNameAscending() {
        List<String> joined = persons
                .stream()
                .sorted((p1, p2) -> p1.getFirstName().compareTo(p2.getFirstName()))
                .unordered()
                .map(p -> p.getFirstName())
                .collect(Collectors.toList());

        assertThat(joined).containsSequence("Basile", "John", "Nico", "Sophie", "Steph");
    }

    @Test
    public void mapPersonsByFirstName() {
        Map<String, List<Person>> mapped = persons
                .stream()
                .collect(Collectors.groupingBy(p -> p.getFirstName()));

        assertThat(mapped.keySet()).containsOnly("Basile", "John", "Nico", "Sophie", "Steph");
        assertThat(mapped.get("Steph")).hasSize(2);
    }

    @Test
    public void mapPersonsByFirstNameAndJoinFirstNames() {

        Map<Person.Gender, String> mapped = persons
                .stream()
                .sorted()
                .collect(Collectors.toMap(
                        p -> p.getGender(),
                        p -> p.getFirstName(),
                        (fn1, fn2) -> fn1 + ", " + fn2));

        assertThat(mapped.keySet()).contains(Person.Gender.female, Person.Gender.male);
        assertThat(mapped.get(Person.Gender.male)).isEqualTo("Steph, Basile, Nico, John");
        assertThat(mapped.get(Person.Gender.female)).isEqualTo("Steph, Sophie");
    }

    @Test
    public void mapPersonsByFirstLetterOfFirstName() {
        Map<Character, List<Person>> mapped = persons
                .stream()
                .collect(Collectors.groupingBy(p -> p.getFirstName().charAt(0)));

        assertThat(mapped.keySet()).containsOnly('B', 'N', 'J', 'S');
        assertThat(mapped.get('S')).hasSize(3);
    }

//    @Test
//    public void countAverageAge() {
//      // marche pas...
//      int averageAge = persons
//                .stream()
//                .collect(Collectors.averagingInt(p -> p.getAge(LocalDate.now())));
//
//    }
}