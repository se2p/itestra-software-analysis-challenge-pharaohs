package com.itestra.software_analyse_challenge.counting;

import java.nio.file.Path;

public interface LineCounterStrategy {
    int countLines(Path path) ;
}
