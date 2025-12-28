package io.github.hyungjun;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MyHttpClientTest {

    @Test
    void connect_success() throws InterruptedException {
        // 서버가 8080 포트에 연결
        Thread thread = new Thread(() -> MyHttpSever.connect(8080));
        thread.start();
        Thread.sleep(3000); // 서버가 시작될 시간을 줌

        // 클라이언트가 서버에게 8080 포트 연결 시도
        MyHttpClient client = new MyHttpClient();
        client.connect("localhost", 8080);

        // 연결이 성공되면 테스트 통과
    }

    @Test
    void connect_failure() throws InterruptedException {
        // 서버가 8080 포트에 연결
        Thread thread = new Thread(() -> MyHttpSever.connect(8080));
        thread.start();
        Thread.sleep(3000); // 서버가 시작될 시간을 줌

        // 클라이언트가 서버에게 8080 포트 연결 시도
        MyHttpClient client = new MyHttpClient();
        assertThatThrownBy(() -> client.connect("localhost", 9090))
                .isInstanceOf(RuntimeException.class);
    }

}
