package io.github.hyungjun;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyHttpResponseTest {

    @Test
    void toText() {
        MyHttpResponse response = new MyHttpResponse(HttpStatus.OK, "<html>Hello</html>");

        String text = response.toText();

        Assertions.assertThat(text).isEqualTo("HTTP/1.1 200 OK\r\n\r\n<html>Hello</html>");
    }
}
