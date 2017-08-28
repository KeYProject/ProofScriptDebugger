package edu.kit.iti.formal.psdbg.gui.controls;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.*;
import java.util.function.Function;

/**
 * @author Alexander Weigl
 * @version 1 (03.06.17)
 */
public class ANTLR4LexerHighlighter {
    private final Function<String, Lexer> factory;

    public ANTLR4LexerHighlighter(Function<String, Lexer> factory) {
        this.factory = factory;
    }


    public StyleSpans<? extends Collection<String>> highlight(String sourcecode) {
        Lexer lexer = factory.apply(sourcecode);
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        try {
            List<? extends Token> tokens = lexer.getAllTokens();
            if (tokens.size() == 0)
                spansBuilder.add(Collections.emptyList(), 0);

            tokens.forEach(token -> {
                String clazz = lexer.getVocabulary().getSymbolicName(token.getType());
                Set<String> clazzes = new HashSet<>();
                clazzes.add(clazz);
                //System.out.format("%25s %s%n", clazz, token.getText());
                spansBuilder.add(
                        clazzes,
                        token.getText().length());
            });

            return spansBuilder.create();
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }
}
