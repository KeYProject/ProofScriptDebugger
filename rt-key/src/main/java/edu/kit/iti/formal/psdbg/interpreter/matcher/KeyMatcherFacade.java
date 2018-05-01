package edu.kit.iti.formal.psdbg.interpreter.matcher;

import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.*;
import de.uka.ilkd.key.logic.op.AbstractSortedOperator;
import de.uka.ilkd.key.logic.op.QuantifiableVariable;
import de.uka.ilkd.key.logic.sort.Sort;
import de.uka.ilkd.key.macros.scripts.ScriptException;
import de.uka.ilkd.key.parser.DefaultTermParser;
import de.uka.ilkd.key.parser.ParserException;
import edu.kit.iti.formal.psdbg.parser.ast.Operator;
import edu.kit.iti.formal.psdbg.parser.ast.Signature;
import lombok.Builder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.key_project.util.collection.ImmutableArray;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Builder
public class KeyMatcherFacade {
    private static Logger logger = LogManager.getLogger(KeyMatcherFacade.class);

    private final DefaultTermParser dtp = new DefaultTermParser();
    private final KeYEnvironment environment;
    private final Sequent sequent;


    public Matchings matches(String pattern, Signature sig) {
        if(pattern.contains("==>")) {
            return matchesSequent(pattern, sig);
        } else {
            return matchesTerm(pattern, sig);
        }

    }

    private Matchings matchesTerm(String pattern, Signature sig) {
        List<Term> positions = new ArrayList<>();
        for (String patternTerm : hasToplevelComma(pattern)) {
            try {
               Term t = dtp.parse(createReader(patternTerm), null, environment.getServices(), environment.getServices().getNamespaces(), null, true);
               positions.add(t);
            } catch (ParserException e) {
                e.printStackTrace();
            }
        }

        KeyTermMatcher keyTermMatcher = new KeyTermMatcher();
        return keyTermMatcher.matchesToplevel(sequent, positions);
    }


    static List<String> hasToplevelComma(String pattern) {
        ArrayList<String> rt = new ArrayList<>();
        char[] c = pattern.toCharArray();
        int level = 0;
        int lastPosition = 0;
        for (int i = 0; i < c.length; i++) {
            if(c[i]==',' && level==0){
                rt.add(pattern.substring(lastPosition, i));
                lastPosition=i;
            }
            if(c[i]=='(' || c[i]=='{')
                level++;
            if(c[i]==')' || c[i]=='}')
                level--;
        }
        if(rt.isEmpty())
            rt.add(pattern);
        return rt;
    }

    private Matchings matchesSequent(String pattern, Signature sig) {

        Sequent seq = null;
        try {
            seq = dtp.parseSeq(createReader(pattern), environment.getServices(), environment.getServices().getNamespaces(), null,true);
        } catch (ParserException e) {
            e.printStackTrace();
        }
        KeyTermMatcher keyTermMatcher = new KeyTermMatcher();
        return keyTermMatcher.matchesSequent(sequent, seq);
    }

    private Reader createReader(String pattern) {
        StringReader sr = new StringReader(pattern);
        return sr;
    }


}
