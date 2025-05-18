package com.itestra.software_analyse_challenge.counting;

import com.itestra.software_analyse_challenge.model.Project;
import com.itestra.software_analyse_challenge.model.SourceFile;

import java.nio.file.Path;
import java.util.List;

public class LineCountingService {

    private final LineCounterStrategy lineCounter;

    public LineCountingService(LineCounterStrategy lineCounter) {
        this.lineCounter = lineCounter;
    }

    public void countLines(List<Project> projects) {
        for (Project project : projects) {
            for (SourceFile sourceFile : project.getFiles()) {
                Path filePath = Path.of(sourceFile.getPath());
                int count = lineCounter.countLines(filePath);
                sourceFile.setNumberOfLines(count);  // ← sets value directly
            }
        }
    }
}
