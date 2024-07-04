package org.example;

import org.example.entities.File;
import org.example.utils.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final String PATH_TO_ROOT_FOLDER = "C:\\firstTask";
    public static final String PATH_TO_RESULT_FILE = "C:\\result\\res.txt";

    public static final String FILE_EXTENSION = ".txt";

    public static void main(String[] args) throws IOException {
        List<File> files = new ArrayList<>();

        IOUtils.readFilesFromFolder(files);
        IOUtils.writeResultToFile(files);
    }
}
