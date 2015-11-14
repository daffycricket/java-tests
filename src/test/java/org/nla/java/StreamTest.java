package org.nla.java;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class StreamTest {

    @Test
    public void sortAndJoin() throws Exception {
        String result = Stream
                .of("z", "a", "b", "f")
                .sorted()
                .collect(Collectors.joining());

        assertThat(result, is(equalTo("abfz")));
    }

    @Test
    public void sortAndJoinWithDelimiter() throws Exception {
        String result = Stream
                .of("z", "a", "b", "f")
                .sorted()
                .collect(Collectors.joining(":"));

        assertThat(result, is(equalTo("a:b:f:z")));
    }

    @Test
    public void sortAndJoinWithDelimiterAndPrefixAndSuffix() throws Exception {
        String result = Stream
                .of("z", "a", "b", "f")
                .sorted()
                .collect(Collectors.joining(":", "START ", " END"));

        assertThat(result, is(equalTo("START a:b:f:z END")));
    }


    @Test
    public void sortAndFilterAndJoin() throws Exception {
        String result = Stream
                .of("z1", "a1", "b2", "f1")
                .filter(c -> c.contains("1"))
                .sorted()
                .collect(Collectors.joining());

        assertThat(result, is(equalTo("a1f1z1")));
    }

    @Test
    public void sortAndFilterAndJoinAndMapWithSimpleFunction() throws Exception {
        String result = Stream
                .of("z1", "a1", "b2", "f1")
                .filter(c -> c.contains("1"))
                .map(s -> s.toUpperCase())
                .sorted()
                .collect(Collectors.joining(""));

        assertThat(result, is(equalTo("A1F1Z1")));
    }

    @Test
    public void sortAndFilterAndJoinAndMapWithSimpleFunctionAndMethodReference() throws Exception {
        String result = Stream
                .of("z1", "a1", "b2", "f1")
                .filter(c -> c.contains("1"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.joining(""));

        assertThat(result, is(equalTo("A1F1Z1")));
    }


    @Test
    public void sortAndFilterAndJoinAndMapWithComplexFunction() throws Exception {
        String result = Stream
                .of("4?lebowski,", "1?your", "5?lebowski!", "3is", "2name")
                .sorted()
                .map(s -> {
                    String intermediate = s.substring(1);
                    if (intermediate.charAt(0) == '?') {
                        return Character.toUpperCase(intermediate.charAt(1)) + intermediate.substring(2);
                    } else {
                        return intermediate;
                    }
                })
                .collect(Collectors.joining(" "));

        assertThat(result, is(equalTo("Your name is Lebowski, Lebowski!")));
    }

    @Test
    public void findFirst() throws Exception {
        String result = Stream
                .of("z1", "a1", "b2", "f1")
                .findFirst()
                .get();

        assertThat(result, is(equalTo("z1")));
    }

    @Test
    public void sortThenfindFirst() throws Exception {
        String result = Stream
                .of("z1", "a1", "b2", "f1")
                .sorted()
                .findFirst()
                .get();

        assertThat(result, is(equalTo("a1")));
    }

    @Test
    public void createStringStreamWithArray() throws Exception {
        String result = Arrays.asList("z1", "a1", "b2", "f1")
                .stream()
                .collect(Collectors.joining());

        assertThat(result, is(equalTo("z1a1b2f1")));
    }

    @Test
    public void createStringStreamWithOf() throws Exception {
        String result = Stream
                .of("z1", "a1", "b2", "f1")
                .collect(Collectors.joining());

        assertThat(result, is(equalTo("z1a1b2f1")));
    }

    @Test
    public void loopOn10Elements() throws Exception {
        IntStream
                .range(1, 10)
                .forEach(System.out::println);
    }
}