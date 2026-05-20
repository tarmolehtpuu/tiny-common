/*
   tiny-common - Tiny Common Helpers
   Copyright 2026 Tarmo Lehtpuu

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package ee.moo.tiny.common.log;

import ee.moo.tiny.common.log.Logger.Format;
import ee.moo.tiny.common.log.Logger.Level;
import org.junit.jupiter.api.Test;

import static ee.moo.tiny.common.util.SystemUtil.captureStdoutLine;
import static ee.moo.tiny.common.util.SystemUtil.captureStdoutLines;
import static org.junit.jupiter.api.Assertions.*;

public class LoggerTest {

    @Test
    public void testIsEnabled() {
        var log1 = new Logger(LoggerTest.class, Level.DEBUG);
        assertTrue(log1.isEnabled(Level.DEBUG));
        assertTrue(log1.isEnabled(Level.INFO));
        assertTrue(log1.isEnabled(Level.WARN));
        assertTrue(log1.isEnabled(Level.ERROR));

        var log2 = new Logger(LoggerTest.class, Level.INFO);
        assertFalse(log2.isEnabled(Level.DEBUG));
        assertTrue(log2.isEnabled(Level.INFO));
        assertTrue(log2.isEnabled(Level.WARN));
        assertTrue(log2.isEnabled(Level.ERROR));

        var log3 = new Logger(LoggerTest.class, Level.WARN);
        assertFalse(log3.isEnabled(Level.DEBUG));
        assertFalse(log3.isEnabled(Level.INFO));
        assertTrue(log3.isEnabled(Level.WARN));
        assertTrue(log3.isEnabled(Level.ERROR));

        var log4 = new Logger(LoggerTest.class, Level.ERROR);
        assertFalse(log4.isEnabled(Level.DEBUG));
        assertFalse(log4.isEnabled(Level.INFO));
        assertFalse(log4.isEnabled(Level.WARN));
        assertTrue(log4.isEnabled(Level.ERROR));
    }

    @Test
    public void testName() {
        assertEquals("LoggerTest", new Logger(LoggerTest.class).getName());
    }

    @Test
    public void testDebug() {
        var log1 = new Logger(LoggerTest.class, Level.DEBUG);
        var log2 = new Logger(LoggerTest.class, Level.INFO);

        var str1 = captureStdoutLine(() -> log1.debug("TERE DEBUG"));
        var str2 = captureStdoutLine(() -> log1.debug("TERE name=%s", "foo"));
        var str3 = captureStdoutLine(() -> log2.debug("TERE DEBUG"));

        assertTrue(str1.contains("[Test worker] DEBUG LoggerTest - TERE DEBUG"));
        assertTrue(str2.contains("[Test worker] DEBUG LoggerTest - TERE name=foo"));
        assertFalse(str3.contains("[Test worker] DEBUG LoggerTest - TERE DEBUG"));
    }

    @Test
    public void testInfo() {
        var log1 = new Logger(LoggerTest.class, Level.INFO);
        var log2 = new Logger(LoggerTest.class, Level.WARN);

        var str1 = captureStdoutLine(() -> log1.info("TERE INFO"));
        var str2 = captureStdoutLine(() -> log1.info("TERE name=%s", "foo"));
        var str3 = captureStdoutLine(() -> log2.info("TERE INFO"));

        assertTrue(str1.contains("[Test worker] INFO  LoggerTest - TERE INFO"));
        assertTrue(str2.contains("[Test worker] INFO  LoggerTest - TERE name=foo"));
        assertFalse(str3.contains("[Test worker] INFO  LoggerTest - TERE INFO"));
    }

    @Test
    public void testWarn() {
        var log1 = new Logger(LoggerTest.class, Level.WARN);
        var log2 = new Logger(LoggerTest.class, Level.ERROR);

        var str1 = captureStdoutLine(() -> log1.warn("TERE WARN"));
        var str2 = captureStdoutLine(() -> log1.warn("TERE name=%s", "foo"));
        var str3 = captureStdoutLine(() -> log2.warn("TERE WARN"));

        assertTrue(str1.contains("[Test worker] WARN  LoggerTest - TERE WARN"));
        assertTrue(str2.contains("[Test worker] WARN  LoggerTest - TERE name=foo"));
        assertFalse(str3.contains("[Test worker] WARN  LoggerTest - TERE WARN"));
    }

    @Test
    public void testError() {
        var log = new Logger(LoggerTest.class, Level.ERROR);
        var str1 = captureStdoutLine(() -> log.error("TERE ERROR"));
        var str2 = captureStdoutLine(() -> log.error("TERE name=%s", "foo"));

        assertTrue(str1.contains("[Test worker] ERROR LoggerTest - TERE ERROR"));
        assertTrue(str2.contains("[Test worker] ERROR LoggerTest - TERE name=foo"));
    }

    @Test
    public void testThrowable() {
        var log = new Logger(LoggerTest.class, Level.DEBUG);
        var e = new RuntimeException("Test Exception");

        var str1 = String.join("\n", captureStdoutLines(() -> log.debug("DEBUG", e)));
        var str2 = String.join("\n", captureStdoutLines(() -> log.info("INFO", e)));
        var str3 = String.join("\n", captureStdoutLines(() -> log.warn("WARN", e)));
        var str4 = String.join("\n", captureStdoutLines(() -> log.error("ERROR", e)));

        assertTrue(str1.contains("[Test worker] DEBUG LoggerTest - DEBUG"));
        assertTrue(str1.contains("java.lang.RuntimeException: Test Exception"));
        assertTrue(str1.contains("at ee.moo.tiny.common.log.LoggerTest.testThrowable"));

        System.out.println(str2);
        assertTrue(str2.contains("[Test worker] INFO  LoggerTest - INFO"));
        assertTrue(str2.contains("java.lang.RuntimeException: Test Exception"));
        assertTrue(str2.contains("at ee.moo.tiny.common.log.LoggerTest.testThrowable"));

        assertTrue(str3.contains("[Test worker] WARN  LoggerTest - WARN"));
        assertTrue(str3.contains("java.lang.RuntimeException: Test Exception"));
        assertTrue(str3.contains("at ee.moo.tiny.common.log.LoggerTest.testThrowable"));

        assertTrue(str4.contains("[Test worker] ERROR LoggerTest - ERROR"));
        assertTrue(str4.contains("java.lang.RuntimeException: Test Exception"));
        assertTrue(str4.contains("at ee.moo.tiny.common.log.LoggerTest.testThrowable"));
    }

    @Test
    public void testJson() {
        var log = new Logger(LoggerTest.class, Level.DEBUG, Format.JSONL);

        var str1 = captureStdoutLine(() -> log.info("HELLO JSON"));
        var str2 = captureStdoutLine(() -> log.info("HELLO name=%s", "JSON"));
        var str3 = captureStdoutLine(() -> log.info("HELLO exception", new RuntimeException("TERE")));

        var exp1 = """
            "level"="INFO","message"="HELLO JSON","thread"="Test worker"
            """.trim();
        var exp2 = """
            "level"="INFO","message"="HELLO name=JSON","thread"="Test worker"
            """.trim();
        var exp3 = """
            "level"="INFO","message"="HELLO exception","thread"="Test worker","stacktrace"=
            """.trim();

        assertTrue(str1.contains(exp1));
        assertTrue(str1.contains("@timestamp"));
        assertTrue(str1.startsWith("{"));
        assertTrue(str1.trim().endsWith("}"));
        assertEquals(1, str1.split("\n").length);

        assertTrue(str2.contains(exp2));
        assertTrue(str2.contains("@timestamp"));
        assertTrue(str2.startsWith("{"));
        assertTrue(str2.trim().endsWith("}"));
        assertEquals(1, str2.split("\n").length);

        assertTrue(str3.contains(exp3));
        assertTrue(str3.contains("@timestamp"));
        assertTrue(str3.startsWith("{"));
        assertTrue(str3.trim().endsWith("}"));
        assertEquals(1, str3.split("\n").length);
        assertTrue(str3.contains("java.lang.RuntimeException: TERE"));
        assertTrue(str3.contains("at ee.moo.tiny.common.log.LoggerTest"));
    }
}
