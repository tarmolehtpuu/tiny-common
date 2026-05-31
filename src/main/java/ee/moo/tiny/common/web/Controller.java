package ee.moo.tiny.common.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ee.moo.tiny.common.log.Logger;
import ee.moo.tiny.common.util.StringUtil;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class Controller implements HttpHandler {

    private static final Logger log = new Logger(Controller.class);

    public static class Response {
        private final int code;
        private final String type;
        private final String body;

        public Response(int code, String type, String body) {
            this.code = code;
            this.type = type;
            this.body = body;
        }

        public int getCode() {
            return code;
        }

        public String getType() {
            return type;
        }

        public String getBody() {
            return body;
        }

        public byte[] getBytes() {
            return body.getBytes(UTF_8);
        }

    }

    public static class TextResponse extends Response {
        public TextResponse(int code, String body) {
            super(code, "text/plain", body);
        }
    }

    public static class PrometheusResponse extends Response {
        public PrometheusResponse(int code, String body) {
            super(code, "text/plain; version=0.0.4; charset=utf-8", body);
        }
    }

    public static class JsonResponse extends Response {
        public JsonResponse(int code, String json) {
            super(code, "application/json", json);
        }
    }

    @Override
    public final void handle(HttpExchange exchange) {
        Response response;
        try {
            var route = route(exchange);
            if (route.isUnknown()) {
                response = error404(exchange);
            } else {
                response = handle(exchange, route);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = error500(exchange);
        }

        try {
            exchange.getResponseHeaders().set("Content-Type", response.getType());
            exchange.sendResponseHeaders(response.getCode(), response.getBytes().length);

            try (var os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }

            if (log.isEnabled(Logger.Level.DEBUG)) {
                log.debug("%s [%d]", exchange.getRequestURI().getPath(), response.getCode());
            }
        } catch (IOException e) {
            log.error("Error sendings response: %s", e.getMessage(), e);
        }
    }

    protected Route route(HttpExchange exchange) {
        return Route.parse(
            exchange.getRequestMethod(),
            exchange.getRequestURI().getPath()
        );
    }

    protected abstract Response handle(HttpExchange exchange, Route route);

    protected Response error(HttpExchange exchange, int code, String message) {
        var accept = exchange.getRequestHeaders().get("Accept");
        if (accept != null && !accept.isEmpty()) {
            for (var value : accept) {
                if (StringUtil.equalsIgnoreCase("application/json", value)) {
                    return new JsonResponse(code, toJson(code, message));
                }
            }
        }

        return new TextResponse(code, message);
    }

    protected Response error500(HttpExchange exchange) {
        return error(exchange, 500, "ERROR 500: Internal Server Error");
    }

    protected Response error401(HttpExchange exchange) {
        return error(exchange, 401, "ERROR 401: Unauthorized");
    }

    protected Response error403(HttpExchange exchange) {
        return error(exchange, 403, "ERROR 403: Forbidden");
    }

    protected Response error404(HttpExchange exchange) {
        return error(exchange, 404, "ERROR 404: Not Found");
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    protected String toJson(int code, String message) {
        var sb = new StringBuilder();
        sb.append('{');

        sb.append('"');
        sb.append("status");
        sb.append('"');
        sb.append(':');
        sb.append(code);
        sb.append(',');

        sb.append('"');
        sb.append("message");
        sb.append('"');
        sb.append(':');
        sb.append('"');
        sb.append(message);
        sb.append('"');

        sb.append('}');

        return sb.toString();
    }
}

