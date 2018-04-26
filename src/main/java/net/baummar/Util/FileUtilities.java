package net.baummar.Util;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtilities {
    public static List<Path> convertListFileToListPath(List<File> listOfFile) {
        return listOfFile.stream().map(File::toPath).collect(Collectors.toList());
    }

    public static List<File> convertPathtoFile(List<Path> listOfPath) {
        return listOfPath.stream().map(Path::toFile).collect(Collectors.toList());
    }

}
