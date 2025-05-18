// SourceFile.java
package com.itestra.software_analyse_challenge.model;

import java.util.*;

public class SourceFile {
    private final String path;
    private final String packageName;
    private List<String> dependencies;
    private final Project project;
    private int numberOfLines;
    private int bonusNumberOfLines;

    public SourceFile(String path, String packageName, Project project) {
        this.path = path;
        this.packageName = packageName;
        this.project = project;
        this.dependencies = new ArrayList<>();
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }
    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }



    public String getPath() { return path; }
    public String getPackageName() { return packageName; }
    public int getNumberOfLines(){return numberOfLines;}
    public  int getBonusNumberOfLines(){return bonusNumberOfLines;}
    public List<String> getProjectDependencies() {
        return dependencies;
    }
    public void setDependencies(Collection<String> dependencies) {
        this.dependencies.clear();
        this.dependencies.addAll(dependencies);
    }

    public void setBonusNumberOfLines(int number){
        bonusNumberOfLines = number;
    }


    public void addImport(String dependency) {
        dependencies.add(dependency);
    }
}