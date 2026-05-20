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

import ee.moo.tiny.common.util.StringUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Logger {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private final String name;
    private final Level level;
    private final Format format;

    public Logger(Class<?> cls) {
        this(cls, null, null);
    }

    public Logger(Class<?> cls, Level level) {
        this(cls, level, null);
    }

    @SuppressWarnings("CodeBlock2Expr")
    public Logger(Class<?> cls, Level level, Format format) {
        var i = cls.getName().lastIndexOf('.');
        if (i == -1) {
            this.name = cls.getName();
        } else {
            this.name = cls.getName().substring(i + 1);
        }

        this.level = Objects.requireNonNullElseGet(level, () -> {
            return Level.valueOf(System.getenv().getOrDefault("LOG_LEVEL", "INFO"));
        });

        this.format = Objects.requireNonNullElseGet(format, () -> {
            return Format.valueOf(System.getenv().getOrDefault("LOG_FORMAT", "TEXT"));
        });
    }

    @SuppressWarnings("EnhancedSwitchMigration")
    public boolean isEnabled(Level level) {
        switch (this.level) {
            case DEBUG:
                return true;
            case INFO:
                return level == Level.INFO || level == Level.WARN || level == Level.ERROR;
            case WARN:
                return level == Level.WARN || level == Level.ERROR;
            default:
                return level == Level.ERROR;
        }
    }

    public String getName() {
        return name;
    }

    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    public void debug(String message, Object... args) {
        log(Level.DEBUG, message, args);
    }

    public void debug(String message, Throwable cause, Object... args) {
        log(Level.DEBUG, message, cause, args);
    }

    public void info(String message) {
        log(Level.INFO, message);
    }

    public void info(String message, Object... args) {
        log(Level.INFO, message, args);
    }

    public void info(String message, Throwable cause, Object... args) {
        log(Level.INFO, message, cause, args);
    }

    public void warn(String message) {
        log(Level.WARN, message);
    }

    public void warn(String message, Object... args) {
        log(Level.WARN, message, args);
    }

    public void warn(String message, Throwable cause, Object... args) {
        log(Level.WARN, message, cause, args);
    }

    public void error(String message) {
        log(Level.ERROR, message);
    }

    public void error(String message, Object... args) {
        log(Level.ERROR, message, args);
    }

    public void error(String message, Throwable cause, Object... args) {
        log(Level.ERROR, message, cause, args);
    }

    public void log(Level level, String message) {
        log(level, message, null, (Object) null);
    }

    public void log(Level level, String message, Object... args) {
        log(level, message, null, args);
    }

    public void log(Level level, String message, Throwable cause, Object... args) {
        if (!isEnabled(level)) {
            return;
        }

        var line = new Line();
        line.setTimestamp(ZonedDateTime.now());
        line.setLevel(level);
        line.setMessage(String.format(message, args));
        line.setThread(Thread.currentThread().getName());
        line.setFormat(format);
        line.setName(name);
        line.setCause(cause);

        line.toString().lines().forEach(System.out::println);
    }

    private static class Line {
        private ZonedDateTime timestamp;
        private Level level;
        private Format format;
        private String name;
        private String message;
        private String thread;
        private Throwable cause;

        public void setTimestamp(ZonedDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public void setLevel(Level level) {
            this.level = level;
        }

        public void setFormat(Format format) {
            this.format = format;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setThread(String thread) {
            this.thread = thread;
        }

        public void setCause(Throwable cause) {
            this.cause = cause;
        }

        public String toString() {
            return format == Format.JSONL ? toJson() : toText();
        }

        public String toText() {
            var sb = new StringBuilder();

            sb.append(timestamp.format(TIME));
            sb.append(' ');
            sb.append('[');
            sb.append(thread);
            sb.append(']');
            sb.append(' ');
            sb.append(StringUtil.rpad(level.toString(), ' ', 5));
            sb.append(' ');
            sb.append(name);
            sb.append(' ');
            sb.append('-');
            sb.append(' ');
            sb.append(message);
            sb.append('\n');

            if (cause != null) {
                var sw = new StringWriter();
                var pw = new PrintWriter(sw);

                cause.printStackTrace(pw);

                sb.append(sw);
            }

            return sb.toString();
        }

        public String toJson() {
            var sb = new StringBuilder();

            sb.append('{');

            sb.append('"');
            sb.append("@timestamp");
            sb.append('"');
            sb.append('=');
            sb.append('"');
            sb.append(timestamp.format(TIME));
            sb.append('"');
            sb.append(',');

            sb.append('"');
            sb.append("level");
            sb.append('"');
            sb.append('=');
            sb.append('"');
            sb.append(level.toString());
            sb.append('"');
            sb.append(',');

            sb.append('"');
            sb.append("message");
            sb.append('"');
            sb.append('=');
            sb.append('"');
            sb.append(message);
            sb.append('"');
            sb.append(',');

            sb.append('"');
            sb.append("thread");
            sb.append('"');
            sb.append('=');
            sb.append('"');
            sb.append(thread);
            sb.append('"');
            sb.append(',');

            if (cause != null) {
                var sw = new StringWriter();
                var pw = new PrintWriter(sw);

                cause.printStackTrace(pw);

                sb.append('"');
                sb.append("stacktrace");
                sb.append('"');
                sb.append('=');
                sb.append('"');
                sb.append(sw.toString().replace("\n", "\\n"));
                sb.append('"');
                sb.append(',');
            }

            if (sb.charAt(sb.length() - 1) == ',') {
                sb.setLength(sb.length() - 1);
            }

            sb.append('}');

            return sb.toString();
        }
    }

    public enum Level {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    public enum Format {
        TEXT,
        JSONL
    }
}
