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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTest {

    @SuppressWarnings("ConstantValue")
    @Test
    public void testIsEmpty() {
        assertTrue(StringUtil.isEmpty(""));
        assertTrue(StringUtil.isEmpty(null));
        Assertions.assertFalse(StringUtil.isEmpty(" "));
        Assertions.assertFalse(StringUtil.isEmpty("foo"));
    }

    @SuppressWarnings({"ConstantValue", "SimplifiableAssertion"})
    @Test
    public void testTrim() {
        assertEquals("foo", StringUtil.trim("foo "));
        assertEquals("foo", StringUtil.trim(" foo"));
        assertEquals("foo", StringUtil.trim(" foo "));
        assertEquals(null, StringUtil.trim(null));
        assertEquals("", StringUtil.trim(""));
        assertEquals("", StringUtil.trim(" "));
        assertEquals("foo", StringUtil.trim("foo"));
        assertEquals("foo", StringUtil.trim("foo       "));
    }

    @Test
    public void testLpad() {
        assertEquals("0001", StringUtil.lpad("1", '0', 4));
        assertEquals("0000", StringUtil.lpad(null, '0', 4));
    }

    @Test
    public void testRpad() {
        assertEquals("100000", StringUtil.rpad("1", '0', 6));
        assertEquals("000000", StringUtil.rpad(null, '0', 6));
    }

    @Test
    public void testLength() {
        assertEquals(0, StringUtil.length(null));
        assertEquals(0, StringUtil.length(""));
        assertEquals(3, StringUtil.length("foo"));
    }

    @Test
    public void testCapitalize() {
        assertNull(StringUtil.capitalize(null));
        assertEquals("", StringUtil.capitalize(""));
        assertEquals("Foo", StringUtil.capitalize("foo"));
        assertEquals("Foo Bar", StringUtil.capitalize("foo-bar"));
        assertEquals("Foo Bar", StringUtil.capitalize("foo_bar"));
    }

    @Test
    public void testCommonPrefix() {
        var strings = List.of(
            "foobar",
            "foobaz",
            "foofoo"
        );

        assertEquals("", StringUtil.commonPrefix(List.of()));
        assertEquals("foo", StringUtil.commonPrefix(strings));
        assertEquals("fooba", StringUtil.commonPrefix("foobar", "foobaz"));
    }

    @Test
    public void testEquals() {
        assertTrue(StringUtil.equals("foo", "foo"));
        assertTrue(StringUtil.equals(null, null));
        assertTrue(StringUtil.equals("", ""));
        assertFalse(StringUtil.equals("foo", "FOO"));
        assertFalse(StringUtil.equals("foo", null));
        assertFalse(StringUtil.equals(null, "foo"));
        assertFalse(StringUtil.equals("foo", "bar"));
    }

    @Test
    public void testEqualsIgnoreCase() {
        assertTrue(StringUtil.equalsIgnoreCase("foo", "foo"));
        assertTrue(StringUtil.equalsIgnoreCase("foo", "FOO"));
        assertTrue(StringUtil.equalsIgnoreCase(null, null));
        assertTrue(StringUtil.equalsIgnoreCase("", ""));
        assertFalse(StringUtil.equalsIgnoreCase("foo", null));
        assertFalse(StringUtil.equalsIgnoreCase(null, "foo"));
        assertFalse(StringUtil.equalsIgnoreCase("foo", "bar"));
    }

    @Test
    public void testIsAlphaNumeric() {
        assertTrue(StringUtil.isAlphaNumeric("fOO123"));
        assertFalse(StringUtil.isAlphaNumeric("$$$"));
        assertFalse(StringUtil.isAlphaNumeric(" "));
        assertFalse(StringUtil.isAlphaNumeric(null));
        assertFalse(StringUtil.isAlphaNumeric(""));
    }

    @Test
    public void testIsAlphaNumericUpper() {
        assertTrue(StringUtil.isAlphaNumericUpper("FOO123"));
        assertFalse(StringUtil.isAlphaNumericUpper("foo123"));
        assertFalse(StringUtil.isAlphaNumericUpper(" "));
        assertFalse(StringUtil.isAlphaNumericUpper("$$$"));
        assertFalse(StringUtil.isAlphaNumericUpper(null));
        assertFalse(StringUtil.isAlphaNumericUpper(""));
    }

    @Test
    public void testIsAlphaNumericLower() {
        assertTrue(StringUtil.isAlphaNumericLower("foo123"));
        assertFalse(StringUtil.isAlphaNumericLower("FOO123"));
        assertFalse(StringUtil.isAlphaNumericLower(" "));
        assertFalse(StringUtil.isAlphaNumericLower("$$$"));
        assertFalse(StringUtil.isAlphaNumericLower(null));
        assertFalse(StringUtil.isAlphaNumericLower(""));
    }

    @Test
    public void testIsAlpha() {
        assertTrue(StringUtil.isAlpha("fOO"));
        assertFalse(StringUtil.isAlpha("fOO123"));
        assertFalse(StringUtil.isAlpha(" "));
        assertFalse(StringUtil.isAlpha("$$$"));
        assertFalse(StringUtil.isAlpha(null));
        assertFalse(StringUtil.isAlpha(""));
    }

    @Test
    public void testIsAlphaLower() {
        assertTrue(StringUtil.isAlphaLower("foo"));
        assertFalse(StringUtil.isAlphaLower("fOO"));
        assertFalse(StringUtil.isAlphaLower(" "));
        assertFalse(StringUtil.isAlphaLower("$$$"));
        assertFalse(StringUtil.isAlphaLower(null));
        assertFalse(StringUtil.isAlphaLower(""));
    }

    @Test
    public void testIsAlphaUpper() {
        assertTrue(StringUtil.isAlphaUpper("FOO"));
        assertFalse(StringUtil.isAlphaUpper("foo"));
        assertFalse(StringUtil.isAlphaUpper(" "));
        assertFalse(StringUtil.isAlphaUpper("$$$"));
        assertFalse(StringUtil.isAlphaUpper(null));
        assertFalse(StringUtil.isAlphaUpper(""));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(StringUtil.isNumeric("123"));
        assertFalse(StringUtil.isNumeric("foo"));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("$$$"));
        assertFalse(StringUtil.isNumeric(null));
        assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsNumber() {
        assertTrue(StringUtil.isNumber("3.14"));
        assertTrue(StringUtil.isNumber("-0.01"));
        assertTrue(StringUtil.isNumber("1e5"));
        assertTrue(StringUtil.isNumber("-2.5E-10"));
        assertTrue(StringUtil.isNumber("6.022e+23"));

        assertFalse(StringUtil.isNumber(null));
        assertFalse(StringUtil.isNumber(""));
        assertFalse(StringUtil.isNumber("e10"));
        assertFalse(StringUtil.isNumber("1.2.3"));
        assertFalse(StringUtil.isNumber("1e"));
        assertFalse(StringUtil.isNumber("e"));
        assertFalse(StringUtil.isNumber("1e+"));
        assertFalse(StringUtil.isNumber("--5"));
        assertFalse(StringUtil.isNumber("12e3.5"));
        assertFalse(StringUtil.isNumber("NaN"));
        assertFalse(StringUtil.isNumber("Infinity"));
    }

    @Test
    public void testIsHex() {
        assertTrue(StringUtil.isHex("cafebabe"));
        assertTrue(StringUtil.isHex("CAFEBABE"));
        assertTrue(StringUtil.isHex("CAFEBABE12"));
        assertFalse(StringUtil.isHex(null));
        assertFalse(StringUtil.isHex(""));
        assertFalse(StringUtil.isHex("cafebab"));
        assertFalse(StringUtil.isHex("foo"));
    }

    @Test
    public void testIsHexLower() {
        assertTrue(StringUtil.isHexLower("cafebabe"));
        assertTrue(StringUtil.isHexLower("cafebabe12"));
        assertFalse(StringUtil.isHexLower("CAFEBABE"));
        assertFalse(StringUtil.isHexLower(null));
        assertFalse(StringUtil.isHexLower(""));
        assertFalse(StringUtil.isHexLower("cafebab"));
        assertFalse(StringUtil.isHexLower("foo"));
    }

    @Test
    public void testIsHexUpper() {
        assertTrue(StringUtil.isHexUpper("CAFEBABE"));
        assertTrue(StringUtil.isHexUpper("CAFEBABE12"));
        assertFalse(StringUtil.isHexUpper("cafebabe"));
        assertFalse(StringUtil.isHexUpper(null));
        assertFalse(StringUtil.isHexUpper(""));
        assertFalse(StringUtil.isHexUpper("CAFEBAB"));
        assertFalse(StringUtil.isHexUpper("FOO"));
    }

    @Test
    public void testIsBase64() {
        assertTrue(StringUtil.isBase64("AB+="));
        assertTrue(StringUtil.isBase64("ABCD"));
        assertFalse(StringUtil.isBase64(null));
        assertFalse(StringUtil.isBase64("$$$"));
        assertFalse(StringUtil.isBase64(""));
    }

    @Test
    public void testIsBase64UrlSafe() {
        assertTrue(StringUtil.isBase64UrlSafe("AB-_"));
        assertFalse(StringUtil.isBase64UrlSafe("AB+="));
        assertFalse(StringUtil.isBase64UrlSafe(null));
        assertFalse(StringUtil.isBase64UrlSafe("$$$"));
        assertFalse(StringUtil.isBase64UrlSafe(""));
    }

    @Test
    public void testIsOnly() {
        assertTrue(StringUtil.isOnly("123321123213123", "123"));
        assertFalse(StringUtil.isOnly("1233211234213123", "123"));
        assertFalse(StringUtil.isOnly(null, "123"));
        assertFalse(StringUtil.isOnly("", "123"));
    }

    @Test
    public void testRandomAlphaNum() {
        var s = StringUtil.randomAlphaNum(10);
        assertEquals(10, s.length());
        assertTrue(StringUtil.isAlphaNumeric(s));
    }

    @Test
    public void testRandomAlphaNumUpper() {
        var s = StringUtil.randomAlphaNumUpper(10);
        assertEquals(10, s.length());
        assertTrue(StringUtil.isAlphaNumericUpper(s));
    }

    @Test
    public void testRandomAlphaNumLower() {
        var s = StringUtil.randomAlphaNumLower(10);
        assertEquals(10, s.length());
        assertTrue(StringUtil.isAlphaNumericLower(s));
    }

    @Test
    public void testRandom() {
        var s = StringUtil.random(100, "01");

        assertEquals(100, s.length());
        assertTrue(StringUtil.isOnly(s, "01"));
    }
}