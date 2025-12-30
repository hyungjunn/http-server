package io.github.hyungjun;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemReader implements FileReader {
    private final String basePath;

    public FileSystemReader(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public String read(String path) {
        try {
            return Files.readString(Path.of(basePath + path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    @Override
    public boolean exists(String path) {
        return Files.exists(Path.of(basePath + path));
    }
}
