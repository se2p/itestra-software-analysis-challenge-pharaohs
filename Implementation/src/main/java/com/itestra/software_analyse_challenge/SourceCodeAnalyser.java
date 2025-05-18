package com.itestra.software_analyse_challenge;

import com.itestra.software_analyse_challenge.counting.BasicLineCounter;
import com.itestra.software_analyse_challenge.counting.LineCounterStrategy;
import com.itestra.software_analyse_challenge.counting.LineCountingService;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class SourceCodeAnalyser {
    //iterate over each base directory to get the files in it
    private static Map<String, List<File>> collectJavaFilesPerProject(File baseDir) {
        Map<String, List<File>> filesPerProjectMap= new HashMap<>();
        for(File projectDir: baseDir.listFiles()){
            if (projectDir.isDirectory()){
                List<File> javaFiles = new ArrayList<>();
                collectFilesPerProjectRecursively(projectDir, javaFiles);
                filesPerProjectMap.put(projectDir.getName(), javaFiles);

            }
        }
        return filesPerProjectMap;
    }

    //takes the ref of the list of files per project to add recursively the inner files
    public static void collectFilesPerProjectRecursively(File insiderDirInProject, List<File> files){
        for(File dir: insiderDirInProject.listFiles()){
            if (dir.isDirectory()){
                collectFilesPerProjectRecursively(dir, files);
            }
            else if(dir.getName().endsWith(".java")){
                files.add(dir);
            }
        }
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
            Map<String, List<File>> projectToFiles = collectJavaFilesPerProject(inputDir);

            // Count lines
            LineCounterStrategy counter = new BasicLineCounter();
            LineCountingService countingService = new LineCountingService(counter);
            Map<String, Integer> lineCounts = countingService.countLines(projectToFiles, inputDir.toPath());

//        // Analyze dependencies
//        DependencyAnalyzer dependencyAnalyzer = new DependencyAnalyzer(inputDir.toPath());
//        dependencyAnalyzer.analyzeProjects(projectToFiles);
//        Map<String, Set<String>> dependencies = dependencyAnalyzer.getFileDependencies();

        // Combine results
        for (Map.Entry<String, Integer> entry : lineCounts.entrySet()) {
            String fileName = entry.getKey();
            int lineCount = entry.getValue();
            result.put(fileName, new Output(lineCount, null));
//            Set<String> fileDependencies = dependencies.getOrDefault(fileName, new HashSet<>());
//            result.put(fileName, new Output(lineCount, new ArrayList<>(fileDependencies)));
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
