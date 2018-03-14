package edu.kit.iti.formal.psdbg.gui.actions.acomplete;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * @author Alexander Weigl
 * @version 1 (12.03.18)
 */
@Value
public class Suggestion implements Comparable<Suggestion> {
    private String text;
    private Category category;
    private int priority;

    public static Suggestion keyword(String keyword) {
        return new Suggestion(keyword, Category.KEYWORD, 4);
    }

    public static Suggestion macro(String name) {
        return new Suggestion(name, Category.MACRO, 3);
    }

    public static Suggestion command(String name) {
        return new Suggestion(name, Category.COMMAND, 2);
    }

    public static Suggestion rule(String name) {
        return new Suggestion(name, Category.RULE, 5);
    }

    public static Suggestion argument(String s) {
        return new Suggestion(s, Category.ATTRIBUTE, 1);
    }

    @Override
    public int compareTo(Suggestion o) {
        return Integer.compare(priority, o.priority);
    }


    @RequiredArgsConstructor
    public static enum Category {
        COMMAND((MaterialDesignIcon.APPLE_KEYBOARD_COMMAND)),
        RULE((MaterialDesignIcon.RULER)),
        MACRO((MaterialDesignIcon.PHARMACY)),
        ATTRIBUTE((MaterialDesignIcon.ATTACHMENT)),
        KEYWORD((MaterialDesignIcon.KEY_VARIANT));

        @Getter
        public final MaterialDesignIcon icon;
    }
}
