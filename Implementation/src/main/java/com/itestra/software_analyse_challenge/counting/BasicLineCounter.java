package com.itestra.software_analyse_challenge.counting;

import com.itestra.software_analyse_challenge.Output;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class BasicLineCounter implements LineCounterStrategy{
    public int countLines(File file){
        int counter = 0;
        Path relativePath = file.toPath();
        System.out.println(relativePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine() )!=null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("//")){
                    counter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return counter;
    }

}
