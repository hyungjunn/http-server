package io.github.hyungjun;

public class MyHttpResponse {
    private final int statusCode;
    private final String statusText;
    private final String body;

    public MyHttpResponse(int statusCode, String statusText, String body) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.body = body;
    }

    public String toText() {
        return "HTTP/1.1 " + this.statusCode + " " + this.statusText + "\r\n\r\n" + this.body;
    }
}
