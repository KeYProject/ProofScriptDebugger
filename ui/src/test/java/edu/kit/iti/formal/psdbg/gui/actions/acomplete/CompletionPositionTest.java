package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Alexander Weigl
 * @version 1 (14.03.18)
 */
public class CompletionPositionTest {
    private CompletionPosition a, b, c, d, e;

    @Before
    public void setup() {
        a = create("abc| def ghi");
        b = create("abc |def ghi");
        c = create("abc\ndef\nghi\n|");
        d = new CompletionPosition("", 3);
        e = create("abc\ndef\n|\nghi\n");
    }

    private static CompletionPosition create(String s) {
        return new CompletionPosition(s.replace("|", ""), s.indexOf('|'));
    }

    @Test
    public void getPrefix() throws Exception {
        assertEquals("abc", a.getPrefix());
        assertEquals("", b.getPrefix());
        assertEquals("", c.getPrefix());
        assertEquals("", d.getPrefix());
        assertEquals("", e.getPrefix());
    }

    @Test
    public void onLineBegin() throws Exception {
        assertFalse(a.onLineBegin());
        assertFalse(b.onLineBegin());
        assertTrue(c.onLineBegin());
        assertTrue(d.onLineBegin());
        assertTrue(e.onLineBegin());
    }

    @Test
    public void filterByPrefix() throws Exception {

    }
}