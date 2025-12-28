package io.github.hyungjun;

import java.net.ServerSocket;

public class MyHttpSever {
    public static void connect(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // 서버가 클라이언트 연결을 기다리는 로직 추가 가능
            System.out.println("Server is listening on port " + port);
            serverSocket.accept(); // 클라이언트 연결 대기
            System.out.println("Client connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
