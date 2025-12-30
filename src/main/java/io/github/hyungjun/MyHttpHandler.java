package io.github.hyungjun;

public class MyHttpHandler {
    private final FileReader fileReader;

    public MyHttpHandler(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public MyHttpResponse handle(MyHttpRequest request) {
        if (!request.getMethod().equals("GET")) {
            return MyHttpResponse.methodNotAllowed();
        }

        String path = request.getPath();
        if (fileReader.exists(path)) {
            return MyHttpResponse.ok(fileReader.read(path));
        }
        return MyHttpResponse.notFound();
    }
}
