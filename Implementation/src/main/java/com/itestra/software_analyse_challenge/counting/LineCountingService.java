package com.itestra.software_analyse_challenge.counting;

import com.itestra.software_analyse_challenge.model.Project;
import com.itestra.software_analyse_challenge.model.SourceFile;

import java.nio.file.Path;
import java.util.List;

public class LineCountingService {
    public enum CountMode {
        BASIC,
        BONUS
    }
    private final CountMode mode;
    private final LineCounterStrategy lineCounter;

    public LineCountingService(LineCounterStrategy strategy, CountMode mode) {
        this.lineCounter = strategy;
        this.mode = mode;

    }


    public void countLines(List<Project> projects) {
        for (Project project : projects) {
            for (SourceFile sourceFile : project.getFiles()) {
                Path filePath = Path.of(sourceFile.getPath());
                int count = lineCounter.countLines(filePath);

                if (mode == CountMode.BASIC) {
                    sourceFile.setNumberOfLines(count);
                } else {
                    sourceFile.setBonusNumberOfLines(count);
                }
            }
        }
    }
}
