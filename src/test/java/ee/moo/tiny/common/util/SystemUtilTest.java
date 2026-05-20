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

import org.junit.jupiter.api.Test;

import static ee.moo.tiny.common.util.SystemUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SystemUtilTest {

    @Test
    public void testCaptureAndSilence() {
        var out = System.out;
        var err = System.err;

        try {
            var s1 = captureStdoutLine(() -> silenceStdout(() -> System.out.println("TERE OUT")));
            var s2 = captureStderrLine(() -> silenceStderr(() -> System.err.println("TERE ERR")));
            var s3 = captureStdoutLine(() -> System.out.println("TERE OUT"));
            var s4 = captureStderrLine(() -> System.err.println("TERE ERR"));

            assertEquals("", s1);
            assertEquals("", s2);
            assertEquals("TERE OUT", s3);
            assertEquals("TERE ERR", s4);

        } finally {
            System.setOut(out);
            System.setErr(err);
        }
    }
}
