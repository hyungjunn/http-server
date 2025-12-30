package io.github.hyungjun;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyHttpHandlerTest {

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

    @Test
    void handle() {
        MyHttpRequest request = MyHttpRequest.from("GET /index.html HTTP/1.1");
        MyHttpHandler handler = new MyHttpHandler(mockFileReader);

        MyHttpResponse response = handler.handle(request);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
        Assertions.assertThat(response.getStatusText()).isEqualTo("OK");
        Assertions.assertThat(response.getBody()).isEqualTo("<html><body><h1>Hello, World!</h1></body></html>");
    }

    @Test
    void handle_not_exist_page() {
        MyHttpRequest request = MyHttpRequest.from("GET /not-exist.html HTTP/1.1");
        MyHttpHandler handler = new MyHttpHandler(mockFileReader);

        MyHttpResponse response = handler.handle(request);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(404);
        Assertions.assertThat(response.getStatusText()).isEqualTo("Not Found");
        Assertions.assertThat(response.getBody()).isEqualTo("<html><body><h1>404 Not Found</h1></body></html>");
    }

    @Test
    void handle_post_request_returns_405() {
        MyHttpRequest request = MyHttpRequest.from("POST /index.html HTTP/1.1");
        MyHttpHandler handler = new MyHttpHandler(mockFileReader);

        MyHttpResponse response = handler.handle(request);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(405);
        Assertions.assertThat(response.getStatusText()).isEqualTo("Method Not Allowed");
    }
}
