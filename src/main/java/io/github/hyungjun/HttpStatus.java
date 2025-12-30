package io.github.hyungjun;

public enum HttpStatus {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String text;

    HttpStatus(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getStatusLine() {
        return code + " " + text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
