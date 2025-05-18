// SourceFile.java
package com.itestra.software_analyse_challenge.model;

import java.util.*;

public class SourceFile {
    private final String path;
    private final String packageName;
    private final List<String> dependencies;
    private final Project project;
    private int numberOfLines;

    public SourceFile(String path, String packageName, Project project) {
        this.path = path;
        this.packageName = packageName;
        this.project = project;
        this.dependencies = new ArrayList<>();
    }

    private Set<String> projectDependencies = new HashSet<>();

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }



    public void setProjectDependencies(Set<String> dependencies) {
        this.projectDependencies = dependencies;
    }

    public String getPath() { return path; }
    public String getPackageName() { return packageName; }
    public Project getProject() { return project; }
    public int getNumberOfLines(){return numberOfLines;}
    public List<String> getProjectDependencies() {
        return dependencies;
    }
    public void addImport(String dependency) {
        dependencies.add(dependency);
    }
}