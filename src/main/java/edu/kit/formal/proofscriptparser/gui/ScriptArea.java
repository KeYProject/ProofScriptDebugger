package edu.kit.formal.proofscriptparser.gui;

import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ScriptLanguageLexer;
import edu.kit.formal.proofscriptparser.lint.LintProblem;
import edu.kit.formal.proofscriptparser.lint.LinterStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.RichTextChange;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Alexander Weigl
 * @version 1 (25.05.17)
 */
public class ScriptArea extends CodeArea {
    private Executor executor = Executors.newSingleThreadExecutor();
    private ObservableMap<Token, LintProblem> problems = FXCollections.emptyObservableMap();

    public ScriptArea() {
        getStylesheets().add(getClass().getResource("script-keywords.css").toExternalForm());
        getStyleClass().add("script-area");
        textProperty().addListener((prop, oldValue, newValue)->{
            computeHighlighting();
        });
               /* .successionEnds(Duration.ofMillis(100))
                .hook(collectionRichTextChange -> this.getUndoManager().mark())
                .supplyTask(this::computeHighlightingAsync).awaitLatest(richChanges())
                .filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                }).subscribe(s -> setStyleSpans(0, s));*/
    }

    private void computeHighlighting() {
        setStyleSpans(0, computeHighlighting(getText()));
        LinterStrategy ls = LinterStrategy.getDefaultLinter();
        List<LintProblem> pl = ls.check(Facade.getAST(CharStreams.fromString(getText())));

        List<Integer> newlines = new ArrayList<>();
        newlines.add(0);
        char[] chars = getText().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\n') {
                newlines.add(i);
            }
        }

        for (LintProblem p : pl) {
            for (Token tok : p.getMarkTokens()) {
                int pos = newlines.get(tok.getLine()) + tok.getCharPositionInLine();
                setStyle(pos, pos + tok.getText().length(), Collections.singleton("problem"));
            }
        }
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
        if (tokens.size() == 0)
            spansBuilder.add(Collections.emptyList(), 0);

        tokens.forEach(token -> {
            String clazz = lexer.getVocabulary().getSymbolicName(token.getType());
            Set<String> clazzes = Collections.singleton(clazz);
            //System.out.format("%25s %s%n", clazz, token.getText());
            spansBuilder.add(
                    clazzes,
                    token.getText().length());
        });


        return spansBuilder.create();
    }


    public ObservableMap<Token, LintProblem> getProblems() {
        return problems;
    }

    public ScriptArea setProblems(ObservableMap<Token, LintProblem> problems) {
        this.problems = problems;
        return this;
    }

    public void setText(String text) {
        insertText(0, text);
    }
}
