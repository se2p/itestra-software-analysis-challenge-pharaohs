package com.itestra.software_analyse_challenge.dependency;

import com.itestra.software_analyse_challenge.model.Project;
import com.itestra.software_analyse_challenge.model.SourceFile;

import java.io.*;
import java.util.*;

public class DependencyAnalyzer {

    private final Map<String, String> packageToProjectMap = new HashMap<>();
    private final Map<String, Set<String>> fileToDirectDeps = new HashMap<>();

    public void analyzeProjects(List<Project> projects) {
        for (Project project : projects) {
            for (SourceFile file : project.getFiles()) {
                String pkg = file.getPackageName();
                if (!pkg.isEmpty()) {
                    packageToProjectMap.put(pkg, project.getName());
                }
            }
        }

        for (Project project : projects) {
            for (SourceFile file : project.getFiles()) {
                Set<String> imports = extractImports(new File(file.getPath()));
                Set<String> directProjects = new HashSet<>();

                for (String imp : imports) {
                    for (Map.Entry<String, String> entry : packageToProjectMap.entrySet()) {
                        String knownPackage = entry.getKey();
                        String targetProject = entry.getValue();

                        if (!targetProject.equals(project.getName()) && imp.startsWith(knownPackage)) {
                            directProjects.add(targetProject);
                        }
                    }
                }

                fileToDirectDeps.put(file.getPath(), directProjects);
            }
        }

        Map<String, Set<String>> projectDeps = new HashMap<>();
        for (Project project : projects) {
            Set<String> allDeps = new HashSet<>();
            for (SourceFile file : project.getFiles()) {
                allDeps.addAll(fileToDirectDeps.getOrDefault(file.getPath(), Set.of()));
            }
            projectDeps.put(project.getName(), allDeps);
        }

        Map<String, Set<String>> projectToTransitiveDeps = computeTransitiveDependencies(projectDeps);

        for (Project project : projects) {
            Set<String> transitiveDeps = projectToTransitiveDeps.getOrDefault(project.getName(), Collections.emptySet());
            for (SourceFile file : project.getFiles()) {
                // Convert Set<String> to List<String>
                file.setDependencies(new ArrayList<>(transitiveDeps));
            }
        }
    }

    private Set<String> extractImports(File file) {
        Set<String> imports = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("import ") && !line.startsWith("import static") && !line.contains("*")) {
                    String imported = line.replace("import", "").replace(";", "").trim();
                    int lastDot = imported.lastIndexOf('.');
                    if (lastDot > 0) {
                        imported = imported.substring(0, lastDot);
                    }
                    imports.add(imported);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imports;
    }

    private Map<String, Set<String>> computeTransitiveDependencies(Map<String, Set<String>> directDeps) {
        Map<String, Set<String>> transitiveDeps = new HashMap<>();

        for (String project : directDeps.keySet()) {
            Set<String> visited = new HashSet<>();
            dfs(project, directDeps, visited);
            visited.remove(project);
            transitiveDeps.put(project, visited);
        }

        return transitiveDeps;
    }

    private void dfs(String current, Map<String, Set<String>> directDeps, Set<String> visited) {
        if (!visited.add(current)) return;
        for (String dep : directDeps.getOrDefault(current, Set.of())) {
            dfs(dep, directDeps, visited);
        }
    }
}
