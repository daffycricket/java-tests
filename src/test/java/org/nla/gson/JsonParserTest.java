package org.nla.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class JsonParserTest {

    private JsonParser jsonParser;

    private String persons;

    @Before
    public void init() {
        jsonParser = new JsonParser();
        File personsFile = new File(getClass().getClassLoader().getResource("persons.json").getFile());
        try (Stream<String> stream = Files.lines(personsFile.toPath())) {
            persons = stream.collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParse() {
        JsonElement root = jsonParser.parse(persons);
        assertThat(root).isNotNull();
    }

    @Test
    public void testParseThenGetFirstChild() {
        JsonElement root = jsonParser.parse(persons);
        JsonObject rootObject = root.getAsJsonObject();
        assertThat(rootObject).isNotNull();
    }
}
