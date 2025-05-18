package com.itestra.software_analyse_challenge;

import com.itestra.software_analyse_challenge.counting.LineCountingService;
import com.itestra.software_analyse_challenge.model.Project;
import com.itestra.software_analyse_challenge.model.SourceFile;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SourceCodeAnalyser {
    //iterate over each base directory to get the files in it
    public static List<Project> collectJavaFilesPerProject(File baseDir) {
        List<Project> projects = new ArrayList<>();

        File[] projectDirs = baseDir.listFiles();
        if (projectDirs != null) {
            for (File projectDir : projectDirs) {
                if (projectDir.isDirectory()) {
                    List<SourceFile> javaFiles = new ArrayList<>();
                    Project proj = new Project(projectDir.getName(), javaFiles, projectDir);
                    projects.add(proj);

                    collectFilesRecursively(projectDir, proj, javaFiles);
                }
            }
        }

        return projects;
    }

    private static void collectFilesRecursively(File currentDir, Project project, List<SourceFile> javaFiles) {
        File[] files = currentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    collectFilesRecursively(file, project, javaFiles);
                } else if (file.getName().endsWith(".java")) {
                    String path = file.getPath();
                    String packageName = extractPackageName(file);
                    javaFiles.add(new SourceFile(path, packageName, project));
                }
            }
        }
    }

    private static String extractPackageName(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("package ")) {
                    return line.replace("package", "").replace(";", "").trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * Your implementation
     *
     * @param input {@link Input} object.
     * @return mapping from filename -> {@link Output} object.
     */
    public static Map<String, Output> analyse(Input input) {
        // TODO insert your Code here.

        // For each file put one Output object to your result map.
        // You can extend the Output object using the functions lineNumberBonus(int), if you did
        // the bonus exercise.

        Map<String, Output> result = new HashMap<>();
        File inputDir = input.getInputDirectory();

        // Count lines
        List<Project> projects = SourceCodeAnalyser.collectJavaFilesPerProject(inputDir);
        LineCountingService service = new LineCountingService(new com.itestra.software_analyse_challenge.service.BasicLineCounter());
        service.countLines(projects);

        // Debugging for counting
        for (Project p : projects) {
            for (SourceFile sf : p.getFiles()) {
                System.out.println(sf.getPath() + ": " + sf.getNumberOfLines());
            }
        }
        for (Project p : projects) {
            for (SourceFile sf : p.getFiles()) {
                new Output(sf.getNumberOfLines(), sf.getProjectDependencies());
                System.out.println(sf.getPath() + ": " + sf.getNumberOfLines());
            }
        }
        return result;

    }


    /**
     * INPUT - OUTPUT
     *
     * No changes below here are necessary!
     */

    public static final Option INPUT_DIR = Option.builder("i")
            .longOpt("input-dir")
            .hasArg(true)
            .desc("input directory path")
            .required(false)
            .build();

    public static final String DEFAULT_INPUT_DIR = String.join(File.separator , Arrays.asList("..", "CodeExamples", "src", "main", "java"));

    private static Input parseInput(String[] args) {
        Options options = new Options();
        Collections.singletonList(INPUT_DIR).forEach(options::addOption);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine commandLine = parser.parse(options, args);
            return new Input(commandLine);
        } catch (ParseException e) {
            formatter.printHelp("help", options);
            throw new IllegalStateException("Could not parse Command Line", e);
        }
    }

    private static void printOutput(Map<String, Output> outputMap) {
        System.out.println("Result: ");
        List<OutputLine> outputLines =
                outputMap.entrySet().stream()
                        .map(e -> new OutputLine(e.getKey(), e.getValue().getLineNumber(), e.getValue().getLineNumberBonus(), e.getValue().getDependencies()))
                        .sorted(Comparator.comparing(OutputLine::getFileName))
                        .collect(Collectors.toList());
        outputLines.add(0, new OutputLine("File", "Source Lines", "Source Lines without Getters and Block Comments", "Dependencies"));
        int maxDirectoryName = outputLines.stream().map(OutputLine::getFileName).mapToInt(String::length).max().orElse(100);
        int maxLineNumber = outputLines.stream().map(OutputLine::getLineNumber).mapToInt(String::length).max().orElse(100);
        int maxLineNumberWithoutGetterAndSetter = outputLines.stream().map(OutputLine::getLineNumberWithoutGetterSetter).mapToInt(String::length).max().orElse(100);
        int maxDependencies = outputLines.stream().map(OutputLine::getDependencies).mapToInt(String::length).max().orElse(100);
        String lineFormat = "| %"+ maxDirectoryName+"s | %"+maxLineNumber+"s | %"+maxLineNumberWithoutGetterAndSetter+"s | %"+ maxDependencies+"s |%n";
        outputLines.forEach(line -> System.out.printf(lineFormat, line.getFileName(), line.getLineNumber(), line.getLineNumberWithoutGetterSetter(), line.getDependencies()));
    }

    public static void main(String[] args) {
        Input input = parseInput(args);
        Map<String, Output> outputMap = analyse(input);
        printOutput(outputMap);
    }
}
