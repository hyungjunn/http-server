package io.github.hyungjun;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MyHttpRequestTest {

    @Test
    @DisplayName("요청 첫줄에서 메서드를 파싱한다")
    void parse() {
        String requestLine = "GET /index.html HTTP/1.1";

        MyHttpRequest myHttpReq = MyHttpRequest.from(requestLine);

        Assertions.assertThat(myHttpReq.getMethod()).isEqualTo("GET");
        Assertions.assertThat(myHttpReq.getPath()).isEqualTo("/index.html");
    }
}
