package io.github.hyungjun;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * HTTP 응답 메시지를 생성하는 클래스.
 *
 * HTTP 응답 메시지 구조:
 * ┌─────────────────────────────────────────┐
 * │ Status Line: HTTP/1.1 200 OK            │  ← 프로토콜 버전, 상태코드, 사유구절
 * ├─────────────────────────────────────────┤
 * │ Header: Content-Type: text/html         │  ← 응답 메타데이터
 * │ Header: Content-Length: 45              │  ← 바디의 바이트 크기
 * ├─────────────────────────────────────────┤
 * │ (빈 줄 - CRLF)                          │  ← 헤더와 바디의 경계
 * ├─────────────────────────────────────────┤
 * │ Body: <html>...</html>                  │  ← 실제 응답 데이터
 * └─────────────────────────────────────────┘
 */
public class MyHttpResponse {
    private final HttpStatus status;
    private final MimeType contentType;
    private final String body;

    public MyHttpResponse(HttpStatus status, MimeType contentType, String body) {
        this.status = status;
        this.contentType = contentType;
        this.body = body;
    }

    public static MyHttpResponse ok(String body, MimeType contentType) {
        return new MyHttpResponse(HttpStatus.OK, contentType, body);
    }

    public static MyHttpResponse notFound() {
        return new MyHttpResponse(HttpStatus.NOT_FOUND, MimeType.HTML,
                "<html><body><h1>404 Not Found</h1></body></html>");
    }

    public static MyHttpResponse methodNotAllowed() {
        return new MyHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, MimeType.HTML,
                "<html><body><h1>405 Method Not Allowed</h1></body></html>");
    }

    public static MyHttpResponse internalServerError() {
        return new MyHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, MimeType.HTML,
                "<html><body><h1>500 Internal Server Error</h1></body></html>");
    }

    /**
     * HTTP 응답 메시지를 문자열로 직렬화한다.
     *
     * 각 줄은 CRLF(\r\n)로 끝나야 하며, 구조는 다음과 같다:
     * 1) Status Line - "HTTP/1.1 {상태코드} {사유구절}\r\n"
     * 2) Headers     - 각 "Key: Value\r\n"
     * 3) 빈 줄       - "\r\n" (헤더 끝을 알림)
     * 4) Body        - 실제 응답 내용
     */
    public String toText() {
        // Body의 바이트 크기를 계산 (문자 수가 아닌 UTF-8 인코딩 기준 바이트 수)
        int contentLength = body.getBytes(UTF_8).length;

        // Status Line: 프로토콜/버전 + 상태코드 + 사유구절
        return "HTTP/1.1 " + status.getStatusLine() + "\r\n"
                // Response Header: 응답 본문의 MIME 타입
                + "Content-Type: " + contentType.getValue() + "\r\n"
                // Response Header: 응답 본문의 바이트 크기 (클라이언트가 body 끝을 판단하는 기준)
                + "Content-Length: " + contentLength + "\r\n"
                // 빈 줄: 헤더 섹션의 끝을 알리는 구분자 (CRLF만 존재)
                + "\r\n"
                // Response Body: 실제 응답 데이터
                + this.body;
    }

    public int getStatusCode() {
        return status.getCode();
    }

    public String getStatusText() {
        return status.getText();
    }

    public MimeType getContentType() {
        return contentType;
    }

    public String getBody() {
        return body;
    }
}
