package edu.kit.formal.psdb.termmatcher;

import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.sort.Sort;
import de.uka.ilkd.key.parser.DefaultTermParser;
import de.uka.ilkd.key.parser.ParserException;
import de.uka.ilkd.key.pp.AbbrevMap;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;

public class MatcherFacadeTest {
    private final static DefaultTermParser dtp = new DefaultTermParser();
    private Services services;
    private AbbrevMap abbrev;
    private NamespaceSet namespace;

    @Before
    public void loadKeyEnv() throws ProblemLoaderException {
        KeYEnvironment<DefaultUserInterfaceControl> env = KeYEnvironment.load(new File("src/test/resources/edu/kit/formal/psdb/termmatcher/test.key"));
        services = env.getServices();
        namespace = env.getServices().getNamespaces();
        abbrev = new AbbrevMap();
    }


    public Term parseKeyTerm(String term) throws ParserException {
        Reader in = new StringReader(term);
        Sort sort = Sort.ANY;
        /*
        Services services = new Services(new JavaProfile());
        AbbrevMap abbrev = new AbbrevMap();
        NamespaceSet namespace = services.getNamespaces();

        Sort Int = new SortImpl(new Name("int"));
        Sort bool = new SortImpl(new Name("boolean"));

        namespace.functions().add(new LogicVariable(new Name("p"), bool));
        namespace.functions().add(new LogicVariable(new Name("q"), bool));
        namespace.functions().add(new Function(new Name("g"), Int));
        namespace.functions().add(new Function(new Name("f"), Int));
        namespace.functions().add(new Function(new Name("h"), Int));

        namespace.functions().add(new Function(new Name("a"), Int));
        namespace.functions().add(new Function(new Name("b"), Int));
        namespace.functions().add(new Function(new Name("c"), Int));
    */

        return dtp.parse(in, sort, services, namespace, abbrev);
    }


    @Test
    public void matches() throws Exception {
        shouldMatch("f(a)", "_");
        shouldMatch("f(a)", "?X", "[{?X=f(a)}]");
        shouldMatch("h(a,a)", "h(?X,?X)", "[{?X=a}]");
        shouldMatch("h(a,b)", "h(?X,?X)", "[]");
    }

    private void shouldMatch(String key, String pattern, String exp) throws ParserException {
        Term term = parseKeyTerm(key);
        Matchings m = MatcherFacade.matches(pattern, term);
        System.out.println(m);
        Assert.assertEquals(exp, m.toString());
    }

    private void shouldMatch(String key, String pattern) throws ParserException {
        Term term = parseKeyTerm(key);
        Matchings m = MatcherFacade.matches(pattern, term);
        System.out.println(m);
    }
}