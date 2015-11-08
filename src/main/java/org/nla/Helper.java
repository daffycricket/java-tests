package org.nla;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Helper {

    public String orderAndSortAndCapitalize(String... args) {

        String toReturn = Stream
                .of(args)
                .sorted()
                .map(s -> s.toUpperCase())
                .collect(Collectors.joining());

        return toReturn;
    }

}
