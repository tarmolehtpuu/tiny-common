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
package ee.moo.tiny.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class SystemUtil {

    private SystemUtil() {
    }

    public static String[] captureStdoutLines(Runnable r) {
        var out = System.out;
        var str = "";

        try {
            var ob = new ByteArrayOutputStream();
            System.setOut(new PrintStream(ob));
            r.run();
            if (ob.size() > 0) {
                str = ob.toString(StandardCharsets.UTF_8);
            }
        } finally {
            System.setOut(out);
        }

        return str.split("\n");
    }

    public static String captureStdoutLine(Runnable r) {
        var lines = captureStdoutLines(r);
        return lines.length >= 1 ? lines[0] : "";
    }

    public static String[] captureStderrLines(Runnable r) {
        var err = System.err;
        var str = "";

        try {
            var ob = new ByteArrayOutputStream();
            System.setErr(new PrintStream(ob));
            r.run();
            if (ob.size() > 0) {
                str = ob.toString(StandardCharsets.UTF_8);
            }
        } finally {
            System.setErr(err);
        }

        return str.split("\n");
    }

    public static String captureStderrLine(Runnable r) {
        var lines = captureStderrLines(r);
        return lines.length >= 1 ? lines[0] : "";
    }

    public static void silenceStdout(Runnable r) {
        var out = System.out;

        try {
            System.setOut(new PrintStream(PrintStream.nullOutputStream()));
            r.run();
        } finally {
            System.setOut(out);
        }
    }

    public static void silenceStderr(Runnable r) {
        var err = System.err;
        try {
            System.setErr(new PrintStream(PrintStream.nullOutputStream()));
            r.run();
        } finally {
            System.setErr(err);
        }
    }
}
