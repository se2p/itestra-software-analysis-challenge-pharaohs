package com.itestra.software_analyse_challenge.service;

import com.itestra.software_analyse_challenge.counting.LineCounterStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BasicLineCounter implements LineCounterStrategy{
    public int countLines(Path path) {
        try {
            return (int) Files.lines(path)
                    .map(String::trim)
                    .filter(line -> !line.isEmpty() && !line.startsWith("//"))
                    .count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
