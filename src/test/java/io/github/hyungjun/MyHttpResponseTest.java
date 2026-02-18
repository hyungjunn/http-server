package io.github.hyungjun;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyHttpResponseTest {

    @Nested
    @DisplayName("toText - HTTP 응답 직렬화")
    class ToText {

        @Test
        @DisplayName("Status Line, Headers, 빈 줄, Body 순서로 직렬화한다")
        void serializesFullResponse() {
            MyHttpResponse response = MyHttpResponse.ok("<html>Hello</html>", MimeType.HTML);

            String text = response.toText();

            // Status Line 확인
            assertThat(text).startsWith("HTTP/1.1 200 OK\r\n");
            // Content-Type 헤더 확인
            assertThat(text).contains("Content-Type: text/html\r\n");
            // Content-Length 헤더 확인 (바이트 기준)
            assertThat(text).contains("Content-Length: 18\r\n");
            // 헤더와 바디 사이 빈 줄 확인
            assertThat(text).contains("\r\n\r\n");
            // Body 확인
            assertThat(text).endsWith("<html>Hello</html>");
        }

        @Test
        @DisplayName("한글 바디의 Content-Length는 UTF-8 바이트 기준으로 계산된다")
        void contentLengthUsesUtf8Bytes() {
            // "안녕" = 6 bytes in UTF-8 (한글 한 글자 = 3 bytes)
            MyHttpResponse response = MyHttpResponse.ok("안녕", MimeType.PLAIN);

            assertThat(response.toText()).contains("Content-Length: 6\r\n");
        }
    }

    @Nested
    @DisplayName("팩토리 메서드")
    class FactoryMethods {

        @Test
        @DisplayName("ok()는 200 상태코드를 반환한다")
        void okStatus() {
            MyHttpResponse response = MyHttpResponse.ok("body", MimeType.HTML);

            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo("body");
        }

        @Test
        @DisplayName("notFound()는 404 상태코드를 반환한다")
        void notFoundStatus() {
            MyHttpResponse response = MyHttpResponse.notFound();

            assertThat(response.getStatusCode()).isEqualTo(404);
            assertThat(response.getBody()).contains("404");
        }

        @Test
        @DisplayName("methodNotAllowed()는 405 상태코드를 반환한다")
        void methodNotAllowedStatus() {
            MyHttpResponse response = MyHttpResponse.methodNotAllowed();

            assertThat(response.getStatusCode()).isEqualTo(405);
        }

        @Test
        @DisplayName("internalServerError()는 500 상태코드를 반환한다")
        void internalServerErrorStatus() {
            MyHttpResponse response = MyHttpResponse.internalServerError();

            assertThat(response.getStatusCode()).isEqualTo(500);
        }
    }
}
