// Project.java
package com.itestra.software_analyse_challenge.model;

import java.io.File;
import java.util.List;

public class Project {
    private final String name;
    private final List<SourceFile> files;
    private final File file;

    public Project(String name, List<SourceFile> projectFiles, File path) {
        this.name = name;
        this.files = projectFiles;
        this.file = path;
    }

    public String getName() { return name; }
    public List<SourceFile> getFiles() { return files; }

    public void addFile(SourceFile file) {
        files.add(file);
    }


    public File getPath(){return file;}
}