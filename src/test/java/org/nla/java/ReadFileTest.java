package org.nla.java;


import org.junit.Before;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class ReadFileTest {

    private String filePath;

    private ClassLoader classLoader;

    @Before
    public void setup() {
        classLoader = getClass().getClassLoader();
        filePath = classLoader.getResource("simple.txt").getPath();
    }

    @Test
    public void readFileContentWithBufferedReader_Java6() {
        StringBuilder fileContentBuilder = new StringBuilder();
        String fileContent;

        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            for (String line = buf.readLine(); line != null; line = buf.readLine()) {
                fileContentBuilder.append(line);
            }
            buf.close();
        } catch (IOException e) {
            fail(e.getMessage());
        } finally {
            fileContent = fileContentBuilder.toString();
        }


        assertThat(fileContent).isNotNull();
        assertThat(fileContent).startsWith("Obviously you’re not a golfer.");
        assertThat(fileContent).endsWith("He fixes the cable.");
    }

    @Test
    public void readFileContentWithBufferedReader_Java7() {
        StringBuilder fileContentBuilder = new StringBuilder();
        String fileContent;

        try (BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            for (String line = buf.readLine(); line != null; line = buf.readLine()) {
                fileContentBuilder.append(line);
            }
        } catch (IOException e) {
            fail(e.getMessage());
        } finally {
            fileContent = fileContentBuilder.toString();
        }

        assertThat(fileContent).isNotNull();
        assertThat(fileContent).startsWith("Obviously you’re not a golfer.");
        assertThat(fileContent).endsWith("He fixes the cable.");
    }

    @Test
    public void readFileContentToStringWithScanner() {
        StringBuilder fileContentBuilder = new StringBuilder();
        String fileContent;

        try (Scanner scanner = new Scanner(new File(filePath), "UTF-8")) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileContentBuilder.append(line);
            }

        } catch (IOException e) {
            fail(e.getMessage());
        } finally {
            fileContent = fileContentBuilder.toString();
        }

        assertThat(fileContent).isNotNull();
        assertThat(fileContent).startsWith("Obviously you’re not a golfer.");
        assertThat(fileContent).endsWith("He fixes the cable.");
    }

    @Test
    public void readFileContentToStringWithNio_Java7() {
        String fileContent = "";
        try {
            fileContent = new String(Files.readAllBytes(new File(filePath).toPath()), "UTF-8");
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertThat(fileContent).isNotNull();
        assertThat(fileContent).startsWith("Obviously you’re not a golfer.");
        assertThat(fileContent).endsWith("He fixes the cable.");
    }

    @Test
    public void readFileContentToListWithNio_Java7() {
        List<String> lines = new ArrayList<>();
        try {
            lines.addAll(Files.readAllLines(new File(filePath).toPath(), Charset.forName("UTF-8")));
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertThat(lines).hasSize(3);
        assertThat(lines).contains("Obviously you’re not a golfer.");
        assertThat(lines).contains("That rug really tied the room together.");
        assertThat(lines).contains("He fixes the cable.");
    }

    @Test
    public void readFileContentToListWithNioAndExcludeLinesWithStreams_Java8() {
        List<String> lines = new ArrayList<>();
        try {
            List<String> rawLines = Files.readAllLines(new File(filePath).toPath(), Charset.forName("UTF-8"));
            lines.addAll(rawLines.stream()
                            .filter(line -> line.contains("f"))
                            .collect(Collectors.toList())
            );
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertThat(lines).hasSize(2);
        assertThat(lines).contains("Obviously you’re not a golfer.");
        assertThat(lines).contains("He fixes the cable.");
    }
}
