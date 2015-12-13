package org.nla.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        new Main();
    }

    public Main() throws IOException {
        JsonParser jsonParser = new JsonParser();
        String jsonContent = new String(Files.readAllBytes(new File(getClass().getClassLoader().getResource("persons.json").getFile()).toPath()));
        JsonElement rootElement = jsonParser.parse(jsonContent);
        printJson(rootElement);
    }


    public void printJson(JsonElement jsonElement) {

        // Check whether jsonElement is JsonObject or not
        if (jsonElement.isJsonObject()) {
            Set<Map.Entry<String, JsonElement>> ens = ((JsonObject) jsonElement).entrySet();
            if (ens != null) {
                // Iterate JSON Elements with Key values
                for (Map.Entry<String, JsonElement> en : ens) {
                    System.out.println(en.getKey() + " : ");
                    printJson(en.getValue());
                }
            }
        }

        // Check whether jsonElement is Arrary or not
        else if (jsonElement.isJsonArray()) {
            JsonArray jarr = jsonElement.getAsJsonArray();
            // Iterate JSON Array to JSON Elements
            for (JsonElement je : jarr) {
                printJson(je);
            }
        }

        // Check whether jsonElement is NULL or not
        else if (jsonElement.isJsonNull()) {
            // print null
            System.out.println("null");
        }
        // Check whether jsonElement is Primitive or not
        else if (jsonElement.isJsonPrimitive()) {
            // print value as String
            System.out.println(jsonElement.getAsString());
        }

    }

}
