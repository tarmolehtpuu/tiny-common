package ee.moo.tiny.common.web;

import com.sun.net.httpserver.HttpExchange;
import ee.moo.tiny.common.IntegrationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ControllerTest extends IntegrationTest {

    @Test
    public void testResponse() {
        var response = new Controller.Response(200, "text/plain", "foo");

        assertEquals(200, response.getCode());
        assertEquals("text/plain", response.getType());
        assertEquals("foo", response.getBody());
        assertArrayEquals(new byte[]{102, 111, 111}, response.getBytes());
    }

    @Test
    public void testTextRespone() throws Exception {
        context("/text", new Controller() {
            @Override
            protected Response handle(HttpExchange exchange, Route route) {
                return new TextResponse(200, "Hello Text");
            }
        });
        startServer();

        var response = GET("/text");

        assertEquals(200, response.statusCode());
        assertEquals("text/plain", response.headers().firstValue("Content-Type").orElseThrow());
        assertEquals("Hello Text", response.body());
    }

    @Test
    public void testJsonResponse() throws Exception {
        context("/json", new Controller() {
            @Override
            protected Response handle(HttpExchange exchange, Route route) {
                return new JsonResponse(200, "[1,2,3]");
            }
        });
        startServer();

        var response = GET("/json");

        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.headers().firstValue("Content-Type").orElseThrow());
        assertEquals("[1,2,3]", response.body());
    }

    @Test
    public void testPrometheusResponse() throws Exception {
        context("/metrics", new Controller() {
            @Override
            protected Response handle(HttpExchange exchange, Route route) {
                return new PrometheusResponse(200, "device_online 1.0");
            }
        });
        startServer();

        var response = GET("/metrics");

        assertEquals(200, response.statusCode());
        assertEquals(
            "text/plain; version=0.0.4; charset=utf-8",
            response.headers().firstValue("Content-Type").orElseThrow()
        );
        assertEquals("device_online 1.0", response.body());
    }

    @Test
    public void test401() throws Exception {
        context("/", new Controller() {
            @Override
            protected Response handle(HttpExchange exchange, Route route) {
                return error401(exchange);
            }
        });
        startServer();

        var r1 = GET("/");
        var r2 = GET("/", "application/json");

        assertEquals(401, r1.statusCode());
        assertEquals("text/plain", r1.headers().firstValue("Content-Type").orElseThrow());
        assertEquals("ERROR 401: Unauthorized", r1.body());

        assertEquals(401, r2.statusCode());
        assertEquals("application/json", r2.headers().firstValue("Content-Type").orElseThrow());
        assertEquals("{\"status\":401,\"message\":\"ERROR 401: Unauthorized\"}", r2.body());
    }

    @Test
    public void test403() throws Exception {
        context("/", new Controller() {
            @Override
            protected Response handle(HttpExchange exchange, Route route) {
                return error403(exchange);
            }
        });
        startServer();

        var r1 = GET("/");
        var r2 = GET("/", "application/json");

        assertEquals(403, r1.statusCode());
        assertEquals("text/plain", r1.headers().firstValue("Content-Type").orElseThrow());
        assertEquals("ERROR 403: Forbidden", r1.body());

        assertEquals(403, r2.statusCode());
        assertEquals("application/json", r2.headers().firstValue("Content-Type").orElseThrow());
        assertEquals("{\"status\":403,\"message\":\"ERROR 403: Forbidden\"}", r2.body());
    }

    @Test
    public void test404() throws Exception {
        context("/", new Controller() {
            @Override
            protected Route route(HttpExchange exchange) {
                return Route.UNKNOWN;
            }

            @Override
            protected Response handle(HttpExchange exchange, Route route) {
                return new TextResponse(200, "Hello");
            }
        });
        startServer();

        var r1 = GET("/");
        var r2 = GET("/", "application/json");

        assertEquals(404, r1.statusCode());
        assertEquals("text/plain", r1.headers().firstValue("Content-Type").orElseThrow());
        assertEquals("ERROR 404: Not Found", r1.body());

        assertEquals(404, r2.statusCode());
        assertEquals("application/json", r2.headers().firstValue("Content-Type").orElseThrow());
        assertEquals("{\"status\":404,\"message\":\"ERROR 404: Not Found\"}", r2.body());
    }

    @Test
    public void test500() throws Exception {
        context("/500", new Controller() {
            @Override
            protected Response handle(HttpExchange exchange, Route route) {
                throw new RuntimeException("Unknown error");
            }
        });
        startServer();

        var r1 = GET("/500");
        var r2 = GET("/500", "application/json");

        assertEquals(500, r1.statusCode());
        assertEquals("text/plain", contentType(r1));
        assertEquals("ERROR 500: Internal Server Error", r1.body());

        assertEquals(500, r2.statusCode());
        assertEquals("application/json", contentType(r2));
        assertEquals("{\"status\":500,\"message\":\"ERROR 500: Internal Server Error\"}", r2.body());
    }
}
