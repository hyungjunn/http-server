package io.github.hyungjun;

public interface FileReader {
    String read(String path);
    boolean exists(String path);
}
