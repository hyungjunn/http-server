package io.github.hyungjun;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * java.net 기반 HTTP/1.1 서버.
 *
 * TCP 소켓으로 클라이언트 연결을 수락하고, HTTP 요청 메시지를 파싱하여 응답한다.
 * HTTP는 TCP 위에서 동작하는 텍스트 기반 프로토콜이다.
 *
 * 요청 처리 흐름:
 * 1. ServerSocket이 TCP 연결 수락 (3-way handshake 이후)
 * 2. InputStream에서 HTTP 요청 메시지 읽기
 *    - Request Line 읽기 (첫 번째 줄)
 *    - Headers 읽기 (빈 줄 전까지)
 *    - Body 읽기 (Content-Length만큼)
 * 3. Handler가 요청을 처리하여 응답 생성
 * 4. OutputStream으로 HTTP 응답 메시지 전송
 */
public class MyHttpServer {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final int port;
    private final MyHttpHandler handler;

    public MyHttpServer(int port, MyHttpHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    public void start() {
        // ServerSocket: TCP 서버 소켓 - 지정된 포트에서 클라이언트 연결을 대기
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                // accept(): TCP 3-way handshake 완료 후 연결된 클라이언트 소켓 반환
                Socket client = serverSocket.accept();
                // 멀티스레드로 각 클라이언트 요청을 병렬 처리
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
            // === 1단계: Request Line 읽기 ===
            // HTTP 요청의 첫 줄: "GET /index.html HTTP/1.1"
            String requestLine = br.readLine();
            if (requestLine == null || requestLine.isBlank()) {
                return; // 빈 요청은 무시
            }

            // === 2단계: Headers 읽기 ===
            // 각 헤더는 "Key: Value" 형태이며, 빈 줄(\r\n만)이 나올 때까지 읽는다
            List<String> headerLines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                headerLines.add(line);
            }
            Map<String, String> headers = MyHttpRequest.parseHeaders(
                    headerLines.toArray(new String[0]));

            // === 3단계: Body 읽기 ===
            // Content-Length 헤더가 있으면 해당 바이트 수만큼 body를 읽는다
            // GET 요청은 보통 body가 없고, POST/PUT 등에서 body를 전송한다
            String body = "";
            String contentLengthValue = headers.getOrDefault("content-length", "0");
            int contentLength = Integer.parseInt(contentLengthValue);
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                int read = br.read(buffer, 0, contentLength);
                if (read > 0) {
                    body = new String(buffer, 0, read);
                }
            }

            // Request Line을 파싱하고, 헤더와 바디를 포함한 완전한 요청 객체 생성
            MyHttpRequest parsedRequest = MyHttpRequest.parseRequestLine(requestLine);
            MyHttpRequest request = MyHttpRequest.of(
                    parsedRequest.getMethod(), parsedRequest.getPath(), parsedRequest.getProtocol(),
                    headers, body);

            // === 4단계: 응답 생성 및 전송 ===
            MyHttpResponse response = handler.handle(request);
            // HTTP 응답 메시지를 UTF-8 바이트로 변환하여 TCP 소켓으로 전송
            os.write(response.toText().getBytes(UTF_8));
            os.flush();
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
