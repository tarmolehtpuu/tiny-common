package ee.moo.tiny.common.web;

import ee.moo.tiny.common.util.StringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RouterTest {

    public record TestCase(
        String ns,
        String method,
        String path,
        Route expected
    ) {
        public TestCase(String method, String path, Route expected) {
            this(null, method, path, expected);
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testParse(TestCase t) {
        if (!StringUtil.isEmpty(t.ns())) {
            assertEquals(t.expected(), Route.parse(t.ns(), t.method(), t.path()));
        } else {
            assertEquals(t.expected(), Route.parse(t.method(), t.path()));
        }
    }

    private static Stream<TestCase> provideTestCases() {
        return Stream.of(
            new TestCase("GET", "/photos", new Route("photos", "index")),
            new TestCase("GET", "/photos/", new Route("photos", "index")),
            new TestCase("GET", "photos/", new Route("photos", "index")),
            new TestCase("DELETE", "/photos", Route.UNKNOWN),
            new TestCase("GET", "/photos/123", new Route("photos", "show", 123L)),
            new TestCase("GET", "/photos/new", new Route("photos", "new")),
            new TestCase("PUT", "/photos/new", Route.UNKNOWN),
            new TestCase("GET", "/photos/tere", new Route("photos", "tere")),
            new TestCase("POST", "/photos", new Route("photos", "create")),
            new TestCase("POST", "/photos/", new Route("photos", "create")),
            new TestCase("GET", "/photos/123/edit", new Route("photos", "edit", 123L)),
            new TestCase("GET", "photos/456/edit", new Route("photos", "edit", 456L)),
            new TestCase("PUT", "/photos/123", new Route("photos", "update", 123L)),
            new TestCase("PATCH", "/photos/123", new Route("photos", "update", 123L)),
            new TestCase("DELETE", "/photos/123", new Route("photos", "delete", 123L)),
            new TestCase("FOO", "/photos/123", Route.UNKNOWN),
            new TestCase("GET", "/photos/123/thumbnail", new Route("photos", "thumbnail", 123L)),
            new TestCase("POST", "/photos/123/toggle", new Route("photos", "toggle", 123L)),
            new TestCase("GET", "/", new Route("home", "index")),
            new TestCase("GET", "", new Route("home", "index")),
            new TestCase("GET", "/foo/bar/baz/omg", Route.UNKNOWN),
            new TestCase(
                "/api/v1",
                "GET",
                "/api/v1/photos/123",
                new Route("/api/v1", "photos", "show", 123L)
            )
        );
    }

    @Test
    public void testIsUnknown() {
        assertTrue(Route.parse("GET", "/foo/bar/baz/omg").isUnknown());
        assertFalse(Route.parse("GET", "/photos/new").isUnknown());
    }
}
