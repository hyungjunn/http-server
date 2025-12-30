package io.github.hyungjun;

public class MyHttpResponse {
    private final HttpStatus status;
    private final MimeType contentType;
    private final String body;

    public static MyHttpResponse ok(String body, MimeType contentType) {
        return new MyHttpResponse(HttpStatus.OK, contentType, body);
    }

    public static MyHttpResponse notFound() {
        return new MyHttpResponse(HttpStatus.NOT_FOUND, MimeType.HTML, "<html><body><h1>404 Not Found</h1></body></html>");
    }

    public static MyHttpResponse methodNotAllowed() {
        return new MyHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, MimeType.HTML, "<html><body><h1>405 Method Not Allowed</h1></body></html>");
    }

    public static MyHttpResponse internalServerError() {
        return new MyHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, MimeType.HTML, "<html><body><h1>500 Internal Server Error</h1></body></html>");
    }

    public MyHttpResponse(HttpStatus status, MimeType contentType, String body) {
        this.status = status;
        this.contentType = contentType;
        this.body = body;
    }

    public String toText() {
        return "HTTP/1.1 " + status.getStatusLine() + "\r\n"
             + "Content-Type: " + contentType.getValue() + "\r\n"
             + "\r\n"
             + this.body;
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
