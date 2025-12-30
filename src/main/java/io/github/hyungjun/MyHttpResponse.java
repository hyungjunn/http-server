package io.github.hyungjun;

public class MyHttpResponse {
    private final HttpStatus status;
    private final String body;

    public static MyHttpResponse ok(String body) {
        return new MyHttpResponse(HttpStatus.OK, body);
    }

    public static MyHttpResponse notFound() {
        return new MyHttpResponse(HttpStatus.NOT_FOUND, "<html><body><h1>404 Not Found</h1></body></html>");
    }

    public static MyHttpResponse methodNotAllowed() {
        return new MyHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "<html><body><h1>405 Method Not Allowed</h1></body></html>");
    }

    public static MyHttpResponse internalServerError() {
        return new MyHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "<html><body><h1>500 Internal Server Error</h1></body></html>");
    }

    public MyHttpResponse(HttpStatus status, String body) {
        this.status = status;
        this.body = body;
    }

    public String toText() {
        return "HTTP/1.1 " + status.getStatusLine() + "\r\n\r\n" + this.body;
    }

    public int getStatusCode() {
        return status.getCode();
    }

    public String getStatusText() {
        return status.getText();
    }

    public String getBody() {
        return body;
    }
}
