package io.github.hyungjun;

public enum MimeType {
    HTML("text/html"),
    CSS("text/css"),
    JS("application/javascript"),
    JSON("application/json"),
    PLAIN("text/plain");

    private final String value;

    MimeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MimeType fromPath(String path) {
        if (path.endsWith(".html")) {
            return HTML;
        } else if (path.endsWith(".css")) {
            return CSS;
        } else if (path.endsWith(".js")) {
            return JS;
        } else if (path.endsWith(".json")) {
            return JSON;
        }
        return PLAIN;
    }
}
