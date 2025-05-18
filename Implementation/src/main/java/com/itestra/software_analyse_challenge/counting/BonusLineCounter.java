package com.itestra.software_analyse_challenge.counting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BonusLineCounter implements LineCounterStrategy {

    private final LineCounterStrategy basicCounter;

    public BonusLineCounter(LineCounterStrategy basicCounter) {
        this.basicCounter = basicCounter;
    }

    @Override
    public int countLines(Path file) {
        try {
            String content = new String(Files.readAllBytes(file));

            // Remove all block comments (/* ... */ and JavaDoc)
            content = content.replaceAll("(?s)/\\*.*?\\*/", "");

            // Remove simple getter methods:
            content = content.replaceAll(
                    "(?m)public\\s+\\w+\\s+get\\w+\\s*\\(\\s*\\)\\s*\\{[^{}]*return[^{};]*;\\s*\\}", ""
            );

            File tempFile = File.createTempFile("cleaned_", ".java");
            Files.write(tempFile.toPath(), content.getBytes());

            int count = basicCounter.countLines(tempFile.toPath());

            tempFile.delete();

            return count;

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
