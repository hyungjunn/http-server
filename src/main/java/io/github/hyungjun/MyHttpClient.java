package io.github.hyungjun;

import java.net.Socket;

public class MyHttpClient {
    public void connect(String ip , int port) {
        // 클라이언트 연결 로직 구현
        System.out.println("Connecting to " + ip + " on port " + port);
        try (Socket socket = new Socket(ip, port)) {
            System.out.println("Connected to server");
            // TODO

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
