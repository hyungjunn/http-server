package io.github.hyungjun;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MyHttpServer {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final int port;
    private final MyHttpHandler handler;

    public MyHttpServer(int port, MyHttpHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                Socket client = serverSocket.accept();
                executorService.submit(() -> handleClient(client));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket client) {
        try (client;
             BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
             OutputStream os = client.getOutputStream()
        ) {
            Thread.sleep(5000); // 인위적인 지연 추가
            String requestLine = br.readLine();
            MyHttpRequest request = MyHttpRequest.from(requestLine);
            MyHttpResponse response = handler.handle(request);
            os.write(response.toText().getBytes(UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        FileReader fileReader = new FileSystemReader("src/main/resources/static");
        MyHttpHandler handler = new MyHttpHandler(fileReader);
        MyHttpServer server = new MyHttpServer(8080, handler);
        server.start();
    }
}
