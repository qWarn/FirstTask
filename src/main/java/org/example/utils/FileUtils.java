package org.example.utils;

import org.example.entities.File;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.remove;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static org.example.Main.FILE_EXTENSION;
import static org.example.Main.PATH_TO_ROOT_FOLDER;

public class FileUtils {
    private static final String TAG = "'";
    private static final String PATH_DELIMITER = "/";

    private static final Pattern REFERENCE_PATTERN = Pattern.compile("require '.*'");

    private FileUtils() {
    }

    public static File mapPathToFile(Path path) {
        String fileBody;
        List<Path> relations = new ArrayList<>();

        try {
            fileBody = join(Files.readAllLines(path), System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Matcher matcher = REFERENCE_PATTERN.matcher(fileBody);
        while (matcher.find()) {
            relations.add(getReferencePath(matcher.group()));
        }

        return new File(getFileNameWithoutExtension(path), path, fileBody, relations);
    }

    public static List<File> sortFiles(List<File> files) {
        List<File> result = new ArrayList<>();
        files.sort(Comparator.comparing(File::getFileName));

        Graph<File, DefaultEdge> directedGraph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        Map<Path, File> filePathToFileMap = files.stream()
                .collect(Collectors.toMap(File::getPath, task -> task));

        for (File file : files) {
            directedGraph.addVertex(file);
            for (Path relation : file.getRelations()) {
                File predecessorTask = filePathToFileMap.get(relation);
                directedGraph.addVertex(predecessorTask);
                directedGraph.addEdge(predecessorTask, file);
            }
        }

        TopologicalOrderIterator<File, DefaultEdge> moreDependencyFirstIterator = new TopologicalOrderIterator<>(
                directedGraph);
        moreDependencyFirstIterator.forEachRemaining(result::add);

        result.forEach(file -> System.out.println(file.getFileName()));

        return result;
    }

    private static Path getReferencePath(String match) {
        return Paths.get(PATH_TO_ROOT_FOLDER + PATH_DELIMITER + substringBetween(match, TAG) + FILE_EXTENSION);
    }

    private static String getFileNameWithoutExtension(Path path) {
        return remove(path.getFileName().toString(), FILE_EXTENSION);
    }
}
