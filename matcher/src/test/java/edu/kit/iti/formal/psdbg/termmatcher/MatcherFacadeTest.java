package edu.kit.iti.formal.psdbg.termmatcher;

import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.logic.Sequent;
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
        String file = getClass().getResource("test.key").toExternalForm().substring(5);

        KeYEnvironment<DefaultUserInterfaceControl> env =
                KeYEnvironment.load(new File(file));
        services = env.getServices();
        namespace = env.getServices().getNamespaces();
        abbrev = new AbbrevMap();
    }

    @Test
    public void error() throws ParserException {
        //        shouldMatch("h2(2,3) = 0",
        //               "?X=0", "[{?X= h2(2,3)}]");

        String s = "[{?X=h2(Z(2(#)),Z(3(#)))}]";
        shouldMatchSeq("h2(2,3) = 0 ==>",
                "?X=0 ==>", s);

        shouldMatchSeq("h2(2,3) = 0 ==> p, !p",
                "?X=0 ==>", s);

        shouldMatchSeq("2 >= 1, h2(1,2) = h2(2,3), h2(2,3) = 0 ==> p, !p",
                "?X=0 ==>", s);
    }

    @Test
    public void matches() throws Exception {
        shouldMatchForm("\\exists int i; \\exists int j; (fint2(i, j) -> fint2(j, i))", "\\exists int i; \\exists int j; (fint2(i, j) -> fint2(j, i))");
        shouldMatch("f(a)", "_");
        shouldMatch("f(a)", "?X", "[{?X=f(a)}]");
        shouldMatch("h(a,a)", "h(?X,?X)", "[{?X=a}]");
        shouldMatch("h(a,b)", "h(?X,?X)", "[]");
        shouldMatch("f(a)", "f(a)");
        shouldMatchForm("pred(a)", "_");
        shouldMatchForm("pred(a)", "...?X...", "[{?X=a}, {?X=pred(a)}]");
        shouldMatchForm("pred(f(a))", "pred(...?X...)", "[{?X=a}, {?X=f(a)}]");
        shouldMatch("i+j", "add(?X,?Y)", "[{?X=i, ?Y=j}]");
        shouldMatch("i+j", "?X+?Y", "[{?X=i, ?Y=j}]");
        shouldMatchForm("p & q", "p & ?X", "[{?X=q}]");
        shouldMatchForm("p & q", "?X & q", "[{?X=p}]");
        shouldMatch("123", "13", "[]");
        shouldMatch("10203", "10203", "[{EMPTY_MATCH=null}]");
        shouldMatch("1*j", "1 + ?Y", "[]");
        shouldMatch("1*j", "1 * ?Y", "[{?Y=j}]");

        shouldMatch("1*j", "?Y * 1", "[{?Y=j}]");


        //shouldMatch("f(a) ==> f(a), f(b)" , "==> f(?X)", [{?X=a}]);
        //shouldMatch("f(a) ==> f(a), f(b)" , "f(a) ==> f(?X)", [{?X=a}]);
        //shouldNotMatch("f(a) ==> f(a), f(b)" , "f(?X) ==> f(?X), f(a)");

    }

    @Test
    public void testBindContext() throws ParserException {
        //shouldMatch("f(a)", "f(a) : ?Y", "[{?Y=f(a)}]");
        //shouldMatch("f(g(a))", "_ \\as ?Y", "[{?Y=f(g(a))}]");
        //shouldMatch("i+i+j", "(?X + ?Y) : ?Z", "[{?X=add(i,i), ?Y=j, ?Z=add(add(i,i),j)}]");

        shouldMatch("g(f(a))", "g( (...a...):?Q ): ?Y",
                "[{EMPTY_MATCH=null, ?Q=f(a), ?Y=g(f(a))}]");

        shouldMatch("f(f(g(a)))", "f( (... g( (...a...):?Q ) ...) : ?R) : ?Y",
                "[{EMPTY_MATCH=null, ?Q=a, ?Y=f(f(g(a))), ?R=f(g(a))}]");
    }


    private void shouldMatch(String key, String pattern) throws ParserException {
        Term term = parseKeyTerm(key);
        Matchings m = MatcherFacade.matches(pattern, term);
        System.out.println(m);
    }

    private void shouldMatch(String key, String pattern, String exp) throws ParserException {
        Term term = parseKeyTerm(key);
        Matchings m = MatcherFacade.matches(pattern, term);
        System.out.println(m);
        Assert.assertEquals(exp, m.toString());
    }

    private void shouldMatchForm(String form, String pattern) throws ParserException {
        Term formula = parserFormula(form);
        Matchings m = MatcherFacade.matches(pattern, formula);
        System.out.println(m);
    }

    private void shouldMatchForm(String form, String pattern, String exp) throws ParserException {
        Term formula = parserFormula(form);
        Matchings m = MatcherFacade.matches(pattern, formula);
        System.out.println(m);
        Assert.assertEquals(exp, m.toString());
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

    public Term parserFormula(String form) throws ParserException {
        Reader in = new StringReader(form);
        Sort sort = Sort.FORMULA;
        return dtp.parse(in, sort, services, namespace, abbrev);
    }

    @Test
    public void semiSeqTest() throws Exception {
        shouldMatchSemiSeq("pred(a), pred(b) ==>", "pred(?X), pred(?Y)", "[{?X=a, ?Y=b}, {?X=b, ?Y=a}]");
        shouldMatchSemiSeq("pred(a), pred(b) ==>", "pred(?X), pred(?X)", "[]");
        shouldMatchSemiSeq("pred(a), pred(f(a)) ==>", "pred(?X), pred(f(?X))", "[{?X=a}]");
        shouldMatchSemiSeq("pred(b), pred(f(a)) ==>", "pred(?X), pred(f(?X))", "[]");
        shouldMatchSemiSeq("pred(a), pred(b) ==>", "pred(a), pred(b)", "[{EMPTY_MATCH=null}]");
        shouldMatchSemiSeq("pred(a), pred(b) ==> qpred(a,b)", "pred(a), pred(b) ==> ", "[{EMPTY_MATCH=null}]");
        shouldMatchSemiSeq("pred(a), pred(b) ==> qpred(a,b)", "pred(?X), pred(?Y)", "[{?X=a, ?Y=b}, {?X=b, ?Y=a}]");

        shouldMatchSemiSeq("pred(b), pred(a) ==>", "pred(...?X...), pred(f(?X))", "[]");
        shouldMatchSemiSeq("pred(b), pred(a) ==>", "pred(...?X...), pred(...?Y...)", "[{?X=a, ?Y=b}, {?X=b, ?Y=a}]");

        shouldMatchSemiSeq("intPred(fint(i+j)) ==>", "intPred(...add(?X, ?y)...)");

    }

    private void shouldMatchSemiSeq(String s, String s1, String exp) throws ParserException {
        Sequent term = parseSeq(s);
        Matchings m = MatcherFacade.matches(s1, term);
        System.out.println(m);
        Assert.assertEquals(exp, m.toString());
    }

    private void shouldMatchSemiSeq(String s, String s1) throws ParserException {
        Sequent term = parseSeq(s);
        Matchings m = MatcherFacade.matches(s1, term);
        System.out.println(m);
    }

    private Sequent parseSeq(String s) throws ParserException {
        Reader in = new StringReader(s);
        return dtp.parseSeq(in, services, namespace, abbrev);
    }

    @Test
    public void seqTest() throws Exception {
        //testcases for empty matches
        shouldMatchSeq("pred(a) ==> pred(a), pred(b)", " ==> _", "[{EMPTY_MATCH=null}]");

        shouldMatchSeq("pred(a) ==> pred(a), pred(b)", "==>", "[{EMPTY_MATCH=null}]");
        shouldMatchSeq("pred(a) ==> pred(a), pred(b)", "_ ==> ", "[{EMPTY_MATCH=null}]");
        shouldMatchSeq("pred(a) ==> pred(a), pred(b)", "_ ==> _", "[{EMPTY_MATCH=null}]");
        shouldMatchSeq("==> pred(a)", "==>", "[{EMPTY_MATCH=null}]");
        shouldMatchSeq("pred(a) ==> ", "==>", "[{EMPTY_MATCH=null}]");


        shouldMatchSeq("pred(a), pred(b) ==> pred(b)", "pred(?X), pred(?Z) ==> pred(?Y)", "[{?X=a, ?Y=b, ?Z=b}, {?X=b, ?Y=b, ?Z=a}]");
        shouldMatchSeq("pred(a), pred(b) ==> pred(b)", "pred(?X), pred(?Z) ==> pred(?X)", "[{?X=b, ?Z=a}]");
        shouldMatchSeq("pred(a), pred(b) ==> pred(b)", "pred(?X), pred(?Z) ==>", "[{?X=a, ?Z=b}, {?X=b, ?Z=a}]");

        shouldMatchSeq(
                "pred(f(a)), pred(b) ==> pred(b)",
                "pred(?X), pred(?Z) ==>",
                "[{?X=b, ?Z=f(a)}, {?X=f(a), ?Z=b}]");

        shouldMatchSeq(
                "pred(f(a)), pred(b) ==> pred(b)",
                "pred(...?X...) ==>",
                "[{?X=a}, {?X=b}, {?X=f(a)}]");

        shouldMatchSeq(
                "fint2(1,2), fint2(2,3), !p ==> pred(a), p",
                "fint2(1, ?X), fint2(?X, ?Y) ==> p",
                "[{EMPTY_MATCH=null, ?X=Z(2(#)), ?Y=Z(3(#))}]");
        shouldMatchSeq("2 >= 1, h2(1,2) = h2(2,3), h2(2,3) = 0 ==> p, !p", "?X=0 ==>", "[{?X=h2(Z(2(#)),Z(3(#)))}]");

        shouldMatchSeq("pred(a) <-> pred(b), pred(a), pred(b) ==> p", "?X <-> ?Y ==> ", "[{?X=pred(a), ?Y=pred(b)}]");
    }

    private void shouldMatchSeq(String seq, String seqPattern, String exp) throws ParserException {
        Sequent sequent = parseSeq(seq);
        Matchings m = MatcherFacade.matches(seqPattern, sequent);
        System.out.println(m);
        Assert.assertEquals(exp, m.toString());
    }

    private void shouldMatchSeq(String seq, String seqPattern) throws ParserException {
        Sequent sequent = parseSeq(seq);
        Matchings m = MatcherFacade.matches(seqPattern, sequent);
        System.out.println(m);
    }
}