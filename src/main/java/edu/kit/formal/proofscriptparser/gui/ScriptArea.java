package edu.kit.formal.proofscriptparser.gui;

import edu.kit.formal.proofscriptparser.ScriptLanguageLexer;
import javafx.concurrent.Task;
import org.antlr.v4.runtime.CharStreams;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Alexander Weigl
 * @version 1 (25.05.17)
 */
public class ScriptArea extends CodeArea {
    private Executor executor = Executors.newSingleThreadExecutor();

    public ScriptArea() {
        getStylesheets().add(getClass().getResource("script-keywords.css").toExternalForm());
        System.out.println(getStylesheets());
        richChanges()//.filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .successionEnds(Duration.ofMillis(100))
                .hook(collectionRichTextChange -> this.getUndoManager().mark())
                .supplyTask(this::computeHighlightingAsync).awaitLatest(richChanges())
                .filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                }).subscribe(s -> setStyleSpans(0, s));
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(getText());
            }
        };
        executor.execute(task);
        return task;
    }

    private StyleSpans<Collection<String>> computeHighlighting(String sourcecode) {
        ScriptLanguageLexer lexer = new ScriptLanguageLexer(CharStreams.fromString(sourcecode));
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        List<? extends org.antlr.v4.runtime.Token> tokens = lexer.getAllTokens();
        tokens.forEach(token -> {
            String clazz = lexer.getVocabulary().getSymbolicName(token.getType());
            System.out.println(clazz);
            spansBuilder.add(
                    Collections.singleton(clazz),
                    token.getText().replaceAll("\\r", "").length());
        });
        return spansBuilder.create();
    }

}
