package ee.moo.tiny.common.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RouterTest {

    public record TestCase(
        String method,
        String path,
        Route expected
    ) {
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void testParse(TestCase t) {
        assertEquals(t.expected(), Route.parse(t.method(), t.path()));
    }

    private static Stream<TestCase> provideTestCases() {
        return Stream.of(
            new TestCase("GET", "/photos", new Route("photos", "index", 0L)),
            new TestCase("GET", "/photos/", new Route("photos", "index", 0L)),
            new TestCase("GET", "photos/", new Route("photos", "index", 0L)),
            new TestCase("DELETE", "/photos", Route.UNKNOWN),
            new TestCase("GET", "/photos/123", new Route("photos", "show", 123L)),
            new TestCase("GET", "/photos/new", new Route("photos", "new", 0L)),
            new TestCase("PUT", "/photos/new", Route.UNKNOWN),
            new TestCase("GET", "/photos/tere", new Route("photos", "tere", 0L)),
            new TestCase("POST", "/photos", new Route("photos", "create", 0L)),
            new TestCase("POST", "/photos/", new Route("photos", "create", 0L)),
            new TestCase("GET", "/photos/123/edit", new Route("photos", "edit", 123L)),
            new TestCase("GET", "photos/456/edit", new Route("photos", "edit", 456L)),
            new TestCase("PUT", "/photos/123", new Route("photos", "update", 123L)),
            new TestCase("PATCH", "/photos/123", new Route("photos", "update", 123L)),
            new TestCase("DELETE", "/photos/123", new Route("photos", "delete", 123L)),
            new TestCase("FOO", "/photos/123", Route.UNKNOWN),
            new TestCase("GET", "/photos/123/thumbnail", new Route("photos", "thumbnail", 123L)),
            new TestCase("POST", "/photos/123/toggle", new Route("photos", "toggle", 123L)),
            new TestCase("GET", "/", new Route("home", "index", 0L)),
            new TestCase("GET", "", new Route("home", "index", 0L)),
            new TestCase("GET", "/foo/bar/baz/omg", Route.UNKNOWN)
        );
    }

    @Test
    public void testIsUnknown() {
        assertTrue(Route.parse("GET", "/foo/bar/baz/omg").isUnknown());
        assertFalse(Route.parse("GET", "/photos/new").isUnknown());
    }
}
