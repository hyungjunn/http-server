package io.github.hyungjun;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyHttpHandlerTest {

    private final FileReader mockFileReader = new FileReader() {
        @Override
        public String read(String path) {
            if (path.equals("/index.html")) {
                return "<html><body><h1>Hello, World!</h1></body></html>";
            }
            return null;
        }

        @Override
        public boolean exists(String path) {
            return path.equals("/index.html");
        }
    };

    private final MyHttpHandler handler = new MyHttpHandler(mockFileReader);

    @Nested
    @DisplayName("GET 요청 처리")
    class GetRequests {

        @Test
        @DisplayName("존재하는 파일 요청시 200 OK와 파일 내용을 반환한다")
        void returnsFileContent() {
            MyHttpRequest request = MyHttpRequest.from("GET /index.html HTTP/1.1");

            MyHttpResponse response = handler.handle(request);

            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getBody()).isEqualTo("<html><body><h1>Hello, World!</h1></body></html>");
        }

        @Test
        @DisplayName("존재하는 HTML 파일 요청시 Content-Type이 text/html이다")
        void returnsHtmlContentType() {
            MyHttpRequest request = MyHttpRequest.from("GET /index.html HTTP/1.1");

            MyHttpResponse response = handler.handle(request);

            assertThat(response.getContentType()).isEqualTo(MimeType.HTML);
        }

        @Test
        @DisplayName("존재하지 않는 파일 요청시 404 Not Found를 반환한다")
        void returns404ForMissingFile() {
            MyHttpRequest request = MyHttpRequest.from("GET /not-exist.html HTTP/1.1");

            MyHttpResponse response = handler.handle(request);

            assertThat(response.getStatusCode()).isEqualTo(404);
            assertThat(response.getBody()).contains("404 Not Found");
        }
    }

    @Nested
    @DisplayName("허용되지 않은 메서드 처리")
    class MethodNotAllowed {

        @Test
        @DisplayName("POST 요청시 405 Method Not Allowed를 반환한다")
        void rejectsPost() {
            MyHttpRequest request = MyHttpRequest.from("POST /index.html HTTP/1.1");

            MyHttpResponse response = handler.handle(request);

            assertThat(response.getStatusCode()).isEqualTo(405);
        }

        @Test
        @DisplayName("DELETE 요청시 405 Method Not Allowed를 반환한다")
        void rejectsDelete() {
            MyHttpRequest request = MyHttpRequest.from("DELETE /index.html HTTP/1.1");

            MyHttpResponse response = handler.handle(request);

            assertThat(response.getStatusCode()).isEqualTo(405);
        }
    }
}
