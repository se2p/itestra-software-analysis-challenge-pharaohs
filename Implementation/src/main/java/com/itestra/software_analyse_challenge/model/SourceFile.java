// SourceFile.java
package com.itestra.software_analyse_challenge.model;

import java.util.HashSet;
import java.util.Set;

public class SourceFile {
    private final String path;
    private final String packageName;
    private final Set<String> imports;
    private final Project project;
    private int numberOfLines;

    public SourceFile(String path, String packageName, Project project) {
        this.path = path;
        this.packageName = packageName;
        this.project = project;
        this.imports = new HashSet<>();
    }

    private Set<String> projectDependencies = new HashSet<>();

    public Set<String> getProjectDependencies() {
        return projectDependencies;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }



    public void setProjectDependencies(Set<String> dependencies) {
        this.projectDependencies = dependencies;
    }

    public String getPath() { return path; }
    public String getPackageName() { return packageName; }
    public Set<String> getImports() { return imports; }
    public Project getProject() { return project; }
    public int getNumberOfLines(){return numberOfLines;}
    public void addImport(String importStatement) {
        imports.add(importStatement);
    }
}