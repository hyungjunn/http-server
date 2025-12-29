package io.github.hyungjun;

public class MyHttpRequest {
    private final String method;
    private final String path;
    private final String protocol;

    public MyHttpRequest(String method, String path, String protocol) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
    }

    public static MyHttpRequest from(String requestLine) {
        String[] group = requestLine.split(" ");
        String method = group[0];
        String path = group[1];
        String protocol = group[2];
        return new MyHttpRequest(method, path, protocol);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
