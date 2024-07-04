package org.example.entities;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class File {

    private String fileName;
    private Path path;
    private String body;
    private List<Path> relations = new ArrayList<>();

    public File() {
    }

    public File(String fileName, Path path, String body, List<Path> relations) {
        this.fileName = fileName;
        this.path = path;
        this.body = body;
        this.relations = relations;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Path> getRelations() {
        return relations;
    }
}
