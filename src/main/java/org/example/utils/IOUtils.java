package org.example.utils;

import org.example.entities.File;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.example.Main.FILE_EXTENSION;
import static org.example.Main.PATH_TO_RESULT_FILE;
import static org.example.Main.PATH_TO_ROOT_FOLDER;

public class IOUtils {

    private static final String FILE_BODY_SEPARATOR = "\n\n";

    private IOUtils() {
    }

    public static void readFilesFromFolder(List<File> files) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(PATH_TO_ROOT_FOLDER))) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(FILE_EXTENSION))
                    .map(FileUtils::mapPathToFile)
                    .forEach(files::add);
        }
    }

    public static void writeResultToFile(List<File> files) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(PATH_TO_RESULT_FILE));

        FileUtils.sortFiles(files).forEach(file -> {
            try {
                writer.write(file.getBody() + FILE_BODY_SEPARATOR);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        writer.close();
    }
}
