package io.github.hyungjun;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MimeTypeTest {

    @Test
    @DisplayName(".html 확장자는 text/html을 반환한다")
    void html() {
        assertThat(MimeType.fromPath("/index.html")).isEqualTo(MimeType.HTML);
    }

    @Test
    @DisplayName(".css 확장자는 text/css를 반환한다")
    void css() {
        assertThat(MimeType.fromPath("/style.css")).isEqualTo(MimeType.CSS);
    }

    @Test
    @DisplayName(".js 확장자는 application/javascript를 반환한다")
    void js() {
        assertThat(MimeType.fromPath("/app.js")).isEqualTo(MimeType.JS);
    }

    @Test
    @DisplayName(".json 확장자는 application/json을 반환한다")
    void json() {
        assertThat(MimeType.fromPath("/data.json")).isEqualTo(MimeType.JSON);
    }

    @Test
    @DisplayName("알 수 없는 확장자는 text/plain을 반환한다")
    void unknown() {
        assertThat(MimeType.fromPath("/file.txt")).isEqualTo(MimeType.PLAIN);
        assertThat(MimeType.fromPath("/image.png")).isEqualTo(MimeType.PLAIN);
    }
}
