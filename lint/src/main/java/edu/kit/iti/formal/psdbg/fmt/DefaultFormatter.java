package edu.kit.iti.formal.psdbg.fmt;


import alice.tuprolog.*;
import com.google.common.base.Strings;
import edu.kit.iti.formal.psdbg.parser.ScriptLanguageLexer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultFormatter implements Formatter {
    private final Prolog prolog;

    private final Theory formattingTheory;

    private boolean inExpression = false;

    private List<? extends Token> tokens;

    private int nested;

    private int pos;

    private List<String> types;

    public DefaultFormatter() throws InvalidLibraryException, IOException, InvalidTheoryException {
        this.prolog = new Prolog();
        //formattingTheory = new Theory(getClass().getResourceAsStream("fmt.pl"));
        formattingTheory = new Theory(new FileInputStream("C:/Users/weigl/IdeaProjects/ProofScriptParser/lint/src/main/resources/fmt.pl"));
        prolog.addTheory(formattingTheory);
        //prolog.loadLibrary();
        //new String[]{"lint/src/main/resources/fmt.pl"}

    }

    @Override
    public String format(String code) {
        ScriptLanguageLexer lexer = new ScriptLanguageLexer(CharStreams.fromString(code));
        StringBuilder sb = new StringBuilder();

        tokens = lexer.getAllTokens().stream()
                .filter(t -> t.getChannel() != Lexer.HIDDEN)
                .collect(Collectors.toList());

        types = tokens.stream()
                .map(Token::getType)
                .map(lexer.getVocabulary()::getSymbolicName)
                //.map(String::toLowerCase)
                .map(s -> String.valueOf(s).toLowerCase())
                .collect(Collectors.toList());


        //System.out.println(types);
        for (pos = 0; pos < tokens.size() - 1; pos++) {
            Token token = tokens.get(pos);

            nested += askForNumber("level", 2);

            int nlCount = 0;
            if ((nlCount = clearLinesBefore()) > 0)
                clearLine(sb, nlCount);
            else {
                int numSpace = 0;
                if ((numSpace = spaceBefore()) > 0)
                    whitespaces(sb, numSpace);
            }
            sb.append(token.getText());

            int numSpace = 0;
            if ((numSpace = spaceAfter()) > 0)
                whitespaces(sb, numSpace);
            else {
                int nlAfter = 0;
                if ((nlAfter = newlineAfter()) > 0) {
                    sb.append(Strings.repeat("\n", nlAfter))
                            .append(Strings.repeat(" ", nested * 4));
                }
            }


        }
        sb.append(tokens.get(pos).getText());
        sb.append("\n");
        return sb.toString();
    }

    private void whitespaces(StringBuilder sb, int numSpace) {
        int i = 0;
        for (i = 0; i < sb.length(); i++) {
            if (sb.charAt(sb.length() - 1 - i) != ' ') {
                break;
            }
        }
        if (i < numSpace)
            sb.append(Strings.repeat(" ", numSpace - i));
    }

    private void clearLine(StringBuilder sb, int nlCount) {
        int i = 0;
        for (i = sb.length() - 1; i >= 0; i--) {
            if (!Character.isWhitespace(sb.charAt(i))) {
                break;
            }
        }
        sb.setLength(i + 1);
        sb.append(Strings.repeat("\n", nlCount))
                .append(Strings.repeat(" ", 4 * nested));
    }

    private int newlineAfter() {
        return askForNumber("nlafter", 2);
    }

    private int spaceBefore() {
        return askForNumber("wsbefore", 2);
    }

    private int spaceAfter() {
        return askForNumber("wsafter", 2);
    }

    private int askForNumber(String name, int numContext) {
        try {
            SolveInfo si = callProlog(name, numContext, 1);
            Term x = si.getVarValue("X");
            if (x instanceof Int)
                return ((Int) x).intValue();
            else
                return 0;
        } catch (MalformedGoalException | NoSolutionException e) {
            // e.printStackTrace();
            return 0;
        }
    }

    private int clearLinesBefore() {
        return askForNumber("onclearline", 5);
    }

    private SolveInfo callProlog(String name, int numContext, int retArgs) throws MalformedGoalException {
        String pctx = getContext(numContext, i -> pos - i - 1);
        String nctx = getContext(numContext, i -> pos + i + 1);

        String query = String.format(
                "%s( [%s], %s, [%s] ,X ).", name, pctx, this.types.get(pos), nctx);
        System.err.format("q: %s%n", query);
        SolveInfo si = prolog.solve(query);
        System.err.format("got %s%n", si);

        return si;
    }

    private String getContext(int numContext, Function<Integer, Integer> access) {
        String[] next = new String[numContext];
        Arrays.fill(next, "empty");
        for (int i = 0; i < numContext; i++) {
            int val = access.apply(i);
            if (0 <= val && val < types.size())
                next[i] = this.types.get(val);
            else break;
        }
        return Arrays.stream(next).reduce((a, b) -> a + ',' + b).orElse("");
    }
}
