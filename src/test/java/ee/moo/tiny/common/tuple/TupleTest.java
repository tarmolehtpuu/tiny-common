package ee.moo.tiny.common.tuple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TupleTest {

    @Test
    public void testPair() {
        var p1 = new Pair<>("foo", 4);
        var p2 = new Pair<>("foo", 4);

        assertEquals(p1, p2);
        assertEquals("foo", p1.first());
        assertEquals(4, p1.second());
        assertEquals("foo", p2.first());
        assertEquals(4, p2.second());
    }

    @Test
    public void testTriple() {
        var t1 = new Triple<>("foo", false, 4);
        var t2 = new Triple<>("foo", false, 4);

        assertEquals(t1, t2);
        assertEquals("foo", t1.first());
        assertEquals(false, t1.second());
        assertEquals(4, t1.third());
        assertEquals("foo", t2.first());
        assertEquals(false, t2.second());
        assertEquals(4, t2.third());
    }
}
