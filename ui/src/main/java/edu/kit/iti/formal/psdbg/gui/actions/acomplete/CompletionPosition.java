package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import lombok.Value;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (13.03.18)
 */
@Value
public class CompletionPosition {
    String text;
    int pos;

    public static int indexOf(String subject, String stopChars, int pos) {
        int start = Math.min(subject.length() - 1, pos);
        char[] chars = subject.toCharArray();
        for (; start < chars.length; start++) {
            if (stopChars.indexOf(chars[start]) >= 0) {
                return start;
            }
        }
        return -1;
    }

    public static int lastIndexOf(String subject, String stopChars, int pos) {
        int start = Math.min(subject.length() - 1, pos);
        char[] chars = subject.toCharArray();
        for (; start >= 0; start--) {
            if (stopChars.indexOf(chars[start]) >= 0) {
                return start + 1;
            }
        }
        if (start == -1 && stopChars.contains("^"))
            return 0;

        return -1;
    }

    public static String find(String subject, String regex, int start) {
        Pattern p = Pattern.compile(regex + ".*", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(subject.substring(start));
        if (m.matches())
            return m.group(1);
        return "";
    }

    public String getPrefix() {
        if (text.isEmpty()) return "";
        int start = lastIndexOf(text, "^\n \t\f", pos);
        if (start == -1)
            return "";
        return text.substring(start, pos + 1).trim();
    }

    public boolean onLineBegin() {
        for (int i = Math.min(text.length() - 1, pos); i >= 0; i--) {
            if (text.charAt(i) == ' ' || text.charAt(i) == '\t') continue;
            if (text.charAt(i) == '\n') return true;
            return false;
        }
        return true;
    }

    public Stream<Suggestion> filterByPrefix(Collection<Suggestion> list) {
        String prefix = getPrefix();
        return list.stream().filter(s -> s.getText().startsWith(prefix));
    }

    public String getCommand() {
        int posNewline = lastIndexOf(text, "^{;", pos);
        if (posNewline >= 0) {
            return find(text, "\\s*(\\w+)\\s", posNewline);
        }
        return "";
    }
}
