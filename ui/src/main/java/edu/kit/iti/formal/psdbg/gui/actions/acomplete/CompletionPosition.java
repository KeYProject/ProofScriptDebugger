package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import lombok.Value;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (13.03.18)
 */
@Value
public class CompletionPosition {
    String text;
    int pos;

    public String getPrefix() {
        if (text.isEmpty()) return "";
        int start = Math.min(text.length() - 1, pos - 1);
        for (; start >= 0; start--) {
            if (Character.isWhitespace(text.charAt(start)))
                break;
        }
        return text.substring(Math.max(0, start), pos).trim();
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
}
