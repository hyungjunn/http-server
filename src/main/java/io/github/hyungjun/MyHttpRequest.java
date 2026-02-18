package io.github.hyungjun;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 요청 메시지를 파싱하여 담는 클래스.
 *
 * HTTP 요청 메시지 구조:
 * ┌─────────────────────────────────────────┐
 * │ Request Line: GET /index.html HTTP/1.1  │  ← 메서드, 경로, 프로토콜 버전
 * ├─────────────────────────────────────────┤
 * │ Header: Host: localhost:8080            │  ← 키: 값 형태의 메타데이터
 * │ Header: Content-Type: text/html         │  ← 각 헤더는 CRLF(\r\n)로 구분
 * │ Header: Content-Length: 13              │
 * ├─────────────────────────────────────────┤
 * │ (빈 줄 - CRLF)                          │  ← 헤더와 바디의 경계
 * ├─────────────────────────────────────────┤
 * │ Body: Hello, World!                     │  ← POST/PUT 등에서 전송하는 데이터
 * └─────────────────────────────────────────┘
 */
public class MyHttpRequest {
    private final String method;
    private final String path;
    private final String protocol;
    private final Map<String, String> headers;
    private final String body;

    public MyHttpRequest(String method, String path, String protocol,
                         Map<String, String> headers, String body) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.headers = headers;
        this.body = body;
    }

    /**
     * === Request Line 파싱 ===
     * HTTP 요청의 첫 번째 줄(Request Line)을 파싱한다.
     * 형식: "{METHOD} {PATH} {PROTOCOL}"
     * 예시: "GET /index.html HTTP/1.1"
     *
     * - METHOD: HTTP 메서드 (GET, POST, PUT, DELETE 등)
     * - PATH: 요청 리소스 경로 (/index.html, /api/users 등)
     * - PROTOCOL: HTTP 프로토콜 버전 (HTTP/1.1)
     */
    public static MyHttpRequest parseRequestLine(String requestLine) {
        // Request Line은 반드시 공백으로 구분된 3개의 토큰이어야 한다
        if (requestLine == null || requestLine.isBlank()) {
            throw new IllegalArgumentException("Request Line이 비어있습니다");
        }

        String[] tokens = requestLine.split(" ");
        if (tokens.length != 3) {
            throw new IllegalArgumentException("잘못된 Request Line 형식: " + requestLine);
        }

        String method = tokens[0];   // HTTP 메서드 (GET, POST 등)
        String path = tokens[1];     // 요청 경로 (/index.html)
        String protocol = tokens[2]; // 프로토콜 버전 (HTTP/1.1)

        return new MyHttpRequest(method, path, protocol, Collections.emptyMap(), "");
    }

    /**
     * === Header 파싱 ===
     * HTTP 헤더 라인들을 파싱하여 Map으로 변환한다.
     * 각 헤더는 "Key: Value" 형식이며, CRLF(\r\n)로 구분된다.
     * 빈 줄(\r\n만 있는 줄)이 나오면 헤더의 끝을 의미한다.
     *
     * RFC 9110 Section 5.1: 헤더 필드 이름은 대소문자를 구분하지 않는다 (case-insensitive).
     * RFC 9112 Section 5: 각 헤더 라인은 "field-name: field-value" 형식이다.
     *
     * 각 헤더 라인을 첫 번째 콜론 기준으로 분리하여 key-value 쌍으로 변환한다.
     * key는 소문자로 정규화하고, value는 앞뒤 공백을 제거한다.
     */
    public static Map<String, String> parseHeaders(String[] headerLines) {
        Map<String, String> headers = new HashMap<>();
        for (String line : headerLines) {
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                String key = parts[0].trim().toLowerCase();
                String value = parts[1].trim();
                headers.put(key, value);
            }
        }
        return headers;
    }

    /**
     * Request Line + Headers + Body를 모두 포함한 완전한 요청 객체를 생성한다.
     */
    public static MyHttpRequest of(String method, String path, String protocol,
                                   Map<String, String> headers, String body) {
        return new MyHttpRequest(method, path, protocol, headers, body);
    }

    /**
     * 하위 호환을 위한 간편 팩토리 - Request Line만으로 생성
     */
    public static MyHttpRequest from(String requestLine) {
        return parseRequestLine(requestLine);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String name) {
        return headers.getOrDefault(name.toLowerCase(), "");
    }

    public String getBody() {
        return body;
    }
}
