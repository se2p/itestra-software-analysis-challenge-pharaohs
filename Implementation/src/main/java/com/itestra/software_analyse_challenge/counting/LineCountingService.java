package com.itestra.software_analyse_challenge.counting;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineCountingService {
    private final LineCounterStrategy counter;

    public LineCountingService(LineCounterStrategy counter) {
        this.counter = counter;
    }

    public Map<String, Integer> countLines(Map<String, List<File>> projectToFiles, Path basePath) {
        Map<String, Integer> result = new HashMap<>();

        for (List<File> files : projectToFiles.values()) {
            for (File file : files) {
                int output = counter.countLines(file);
                String relativePath = basePath.relativize(file.toPath()).toString();
                result.put(relativePath, output);
            }
        }

        return result;
    }
}
