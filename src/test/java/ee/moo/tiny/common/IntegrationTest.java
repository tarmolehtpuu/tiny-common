package ee.moo.tiny.common;

import com.sun.net.httpserver.HttpServer;
import ee.moo.tiny.common.log.Logger;
import ee.moo.tiny.common.web.Controller;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;

public class IntegrationTest {

    public static final String APPLICATION_HOST = "127.0.0.1";
    public static final int APPLICATION_PORT = 8081;

    private static final Logger log = new Logger(IntegrationTest.class);

    protected HttpServer server;

    protected HttpClient client;

    @BeforeEach
    public void beforeEach() throws IOException {
        server = HttpServer.create(new InetSocketAddress(APPLICATION_HOST, APPLICATION_PORT), 0);
        client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofMillis(2000))
            .build();
    }

    @AfterEach
    public void afterEach() {
        log.info("HttpServer shutting down...");
        server.stop(1);
        log.info("HttpServer stopped");
    }

    public void context(String path, Controller controller) {
        server.createContext(path, controller);
        log.info("Context: %s -> %s", path, controller.getClass().getSimpleName());
    }

    public URI applicationUri(String path) throws URISyntaxException {
        return new URI(String.format("http://%s:%d%s", APPLICATION_HOST, APPLICATION_PORT, path));
    }

    public String contentType(HttpResponse<String> response) {
        return response.headers().firstValue("Content-Type").orElseThrow();
    }

    public HttpResponse<String> GET(String path) throws Exception {
        return GET(path, "text/plain");
    }

    public HttpResponse<String> GET(String path, String accept) throws Exception {
        var request = HttpRequest.newBuilder()
            .timeout(Duration.ofMillis(2000))
            .header("Accept", accept)
            .uri(applicationUri(path))
            .GET()
            .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void startServer() throws IOException {
        log.info("HttpServer starting...");

        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();

        log.info("Started HttpServer on :%s:%d", APPLICATION_HOST, APPLICATION_PORT);
    }
}
