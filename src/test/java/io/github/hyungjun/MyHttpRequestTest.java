package io.github.hyungjun;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MyHttpRequestTest {

    @Nested
    @DisplayName("parseRequestLine")
    class ParseRequestLine {

        @Test
        @DisplayName("GET 요청의 Request Line에서 메서드, 경로, 프로토콜을 파싱한다")
        void parsesGetRequest() {
            MyHttpRequest request = MyHttpRequest.parseRequestLine("GET /index.html HTTP/1.1");

            assertThat(request.getMethod()).isEqualTo("GET");
            assertThat(request.getPath()).isEqualTo("/index.html");
            assertThat(request.getProtocol()).isEqualTo("HTTP/1.1");
        }

        @Test
        @DisplayName("POST 요청의 Request Line을 파싱한다")
        void parsesPostRequest() {
            MyHttpRequest request = MyHttpRequest.parseRequestLine("POST /api/users HTTP/1.1");

            assertThat(request.getMethod()).isEqualTo("POST");
            assertThat(request.getPath()).isEqualTo("/api/users");
        }

        @Test
        @DisplayName("null 입력시 IllegalArgumentException을 던진다")
        void throwsOnNull() {
            assertThatThrownBy(() -> MyHttpRequest.parseRequestLine(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("비어있습니다");
        }

        @Test
        @DisplayName("빈 문자열 입력시 IllegalArgumentException을 던진다")
        void throwsOnBlank() {
            assertThatThrownBy(() -> MyHttpRequest.parseRequestLine(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("토큰이 3개가 아니면 IllegalArgumentException을 던진다")
        void throwsOnMalformed() {
            assertThatThrownBy(() -> MyHttpRequest.parseRequestLine("GET /index.html"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("잘못된 Request Line");
        }

        @Test
        @DisplayName("Request Line만 파싱하면 헤더와 바디는 비어있다")
        void headersAndBodyAreEmpty() {
            MyHttpRequest request = MyHttpRequest.parseRequestLine("GET / HTTP/1.1");

            assertThat(request.getHeaders()).isEmpty();
            assertThat(request.getBody()).isEmpty();
        }
    }

    @Nested
    @DisplayName("parseHeaders")
    class ParseHeaders {

        @Test
        @DisplayName("빈 배열이면 빈 Map을 반환한다")
        void emptyArray() {
            assertThat(MyHttpRequest.parseHeaders(new String[]{})).isEmpty();
        }

        @Test
        @DisplayName("헤더 라인을 key-value 쌍으로 파싱한다")
        void parsesKeyValue() {
            String[] lines = {"Host: localhost:8080", "Accept: text/html"};

            var headers = MyHttpRequest.parseHeaders(lines);

            assertThat(headers)
                    .containsEntry("host", "localhost:8080")
                    .containsEntry("accept", "text/html");
        }

        @Test
        @DisplayName("key는 소문자로 정규화된다")
        void keysAreLowerCased() {
            String[] lines = {"Content-Type: text/html"};

            var headers = MyHttpRequest.parseHeaders(lines);

            assertThat(headers).containsKey("content-type");
            assertThat(headers).doesNotContainKey("Content-Type");
        }

        @Test
        @DisplayName("value에 콜론이 포함되어도 올바르게 파싱한다")
        void handlesColonInValue() {
            String[] lines = {"Host: localhost:8080"};

            var headers = MyHttpRequest.parseHeaders(lines);

            // split(":", 2) 덕분에 value의 콜론이 보존된다
            assertThat(headers.get("host")).isEqualTo("localhost:8080");
        }

        @Test
        @DisplayName("콜론이 없는 잘못된 헤더 라인은 무시한다")
        void ignoresMalformedLines() {
            String[] lines = {"InvalidHeader", "Host: localhost"};

            var headers = MyHttpRequest.parseHeaders(lines);

            assertThat(headers).hasSize(1).containsKey("host");
        }
    }
}
