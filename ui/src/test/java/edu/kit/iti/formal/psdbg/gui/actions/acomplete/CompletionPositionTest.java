package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Alexander Weigl
 * @version 1 (14.03.18)
 */
public class CompletionPositionTest {
    private CompletionPosition a, b, c, d, e, f, g, h;

    private static CompletionPosition create(String s) {
        return new CompletionPosition(s.replace("|", ""), s.indexOf('|'));
    }

    @Test
    public void find() throws Exception {
        assertEquals("abc",
                CompletionPosition.find("abc\na", "\\s*(\\w+)\\s", 0));
        assertEquals("abc",
                CompletionPosition.find("abc\ndef\n", "\\s*(\\w+)\\s", 0));
        assertEquals("abc",
                CompletionPosition.find("abc\ndef\n|\nghi\n", "\\s*(\\w+)\\s", 0));
    }

    @Test
    public void getCommand() {
        assertEquals("abc", a.getCommand());
        assertEquals("abc", b.getCommand());
        assertEquals("abc", c.getCommand());
        assertEquals("", d.getCommand());
        assertEquals("abc", e.getCommand());
        assertEquals("multiLineRule", f.getCommand());
        assertEquals("multiLineRule", g.getCommand());
        assertEquals("multiLineRule", h.getCommand());
    }

    @Before
    public void setup() {
        a = create("abc| def ghi");
        b = create("abc |def ghi");
        c = create("abc\ndef\nghi\n|");
        d = new CompletionPosition("", 3);
        e = create("abc\ndef\n|\nghi\n");
        f = create("multiLineRule\nabc=1\ndef=2\n |;");
        g = create("abc;\n\nmultiLineRule   a=\n2 abc  =  1|        \ndef=2\n;");
        h = create("foreach{\n\nmultiLineRule   a=\n2 abc  =  1|       \ndef=2\n; }");
    }

    @Test
    public void getPrefix() throws Exception {
        assertEquals("abc", a.getPrefix());
        assertEquals("", b.getPrefix());
        assertEquals("", c.getPrefix());
        assertEquals("", d.getPrefix());
        assertEquals("", e.getPrefix());
        assertEquals("", f.getPrefix());
        assertEquals("", g.getPrefix());
        assertEquals("", h.getPrefix());
    }

    @Test
    public void onLineBegin() throws Exception {
        assertFalse(a.onLineBegin());
        assertFalse(b.onLineBegin());
        assertTrue(c.onLineBegin());
        assertTrue(d.onLineBegin());
        assertTrue(e.onLineBegin());
        assertFalse(f.onLineBegin());//TODO off-by-one
        assertFalse(g.onLineBegin());
        assertFalse(h.onLineBegin());
    }

    @Test
    public void filterByPrefix() throws Exception {

    }
}