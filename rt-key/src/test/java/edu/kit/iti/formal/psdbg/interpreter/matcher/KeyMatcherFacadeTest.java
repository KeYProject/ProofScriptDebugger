package edu.kit.iti.formal.psdbg.interpreter.matcher;

import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.logic.Sequent;
import de.uka.ilkd.key.logic.Term;
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

public class KeyMatcherFacadeTest {

    private final static DefaultTermParser dtp = new DefaultTermParser();
    private Services services;
    private AbbrevMap abbrev;
    private NamespaceSet namespace;
    private KeYEnvironment keyenv;


    @Before
    public void loadKeyEnv() throws ProblemLoaderException {
        String file = getClass().getResource("test.key").toExternalForm().substring(5);

        KeYEnvironment<DefaultUserInterfaceControl> env =
                KeYEnvironment.load(new File(file));
        keyenv = env;
        services = env.getServices();
        namespace = env.getServices().getNamespaces();
        abbrev = new AbbrevMap();

    }


    /**
     * Actually testcases for KeyTermMatcher not for Facade
     * @throws Exception
     */
    @Test
    public void matchTerms() throws Exception {
        shouldMatchT("f(a)", "?");
        shouldMatchT("f(a)", "f(a)");

        shouldMatchT("f(a)", "?X", "[{?X=f(a)}]");
        shouldMatchT("h(a,a)", "h(?X,?X)", "[{?X=a}]");
        shouldMatchT("h(a,b)", "h(?X,?X)", "[]");

    }


    @Test
    public void matchSeq() throws Exception {
       //atm missing is catching the toplevel formula
        //shouldMatch("==> pred(a)", "==> pred(?)", "[]");


        shouldMatch("h2(2,2) = 0 ==>p, !p", "?X=0 ==>", "[{EMPTY_MATCH=null, ?X=h2(Z(2(#)),Z(2(#)))}]");
        shouldMatch("2 >= 1, h2(1,2) = h2(2,3), h2(2,3) = 0 ==> p, !p", "?X=0 ==>", "[{EMPTY_MATCH=null, ?X=Z(2(#))}, {EMPTY_MATCH=null, ?X=h2(Z(2(#)),Z(3(#)))}]");

        shouldMatch("p ==>", "p ==>", "[{EMPTY_MATCH=null}]");
        shouldMatch("==> pred(a), q", "==> pred(?X:S), q", "[{?X=a}]");
        shouldMatch("==> p, q", "==> ?X:Formula", "[{?X=p}, {?X=q}]");
        shouldMatch("==> p, q", "==> p, q","[{EMPTY_MATCH=null}]");
        shouldMatch("==> p, q", "==> q, p", "[{EMPTY_MATCH=null}]");
        shouldMatch("==> pred(a), q", "==> pred(?X)", "[{?X=a}]");
        shouldMatch("h2(2,2) = 0 ==>", "?X=0 ==>", "[{?X=h2(Z(2(#)),Z(2(#)))}]");
        shouldMatch("==>f(g(a))= 0", "==> f(...a...) = 0", "[{EMPTY_MATCH=null}]");
    }

    @Test
    public void negativeMatchSeq() throws Exception {
        shouldNotMatch("p==>", "==> p");

    }

    @Test
    public void testBindContext() throws Exception {
        //shouldMatch("f(a)", "f(a) : ?Y", "[{?Y=f(a)}]");
        //shouldMatch("f(g(a))", "_ \\as ?Y", "[{?Y=f(g(a))}]");
        //shouldMatch("i+i+j", "(?X + ?Y) : ?Z", "[{?X=add(i,i), ?Y=j, ?Z=add(add(i,i),j)}]");


        shouldMatch("==>f(g(a))= 0", "==> f((...a...):?G) = 0", "[{?G=g(a), EMPTY_MATCH=null}]");
        shouldMatch("==>f(f(g(a)))= 0", "==> f((...a...):?G) = 0", "[{?G=f(g(a)), EMPTY_MATCH=null}]");
        shouldMatch("==>f(f(g(a)))= 0", "==> f((...g(...a...)...):?G) = 0", "[{?G=f(g(a)), EMPTY_MATCH=null}]");
        shouldMatch("==>f(f(g(a)))= 0", "==> f((...g((...a...):?Q)...):?G) = 0", "[{?G=f(g(a)), EMPTY_MATCH=null, ?Q=a}]");
        shouldMatch("pred(f(a)) ==>", "pred((...a...):?Q) ==>","[{EMPTY_MATCH=null, ?Q=f(a)}]");
        shouldMatch("p ==>", "(p):?X:Formula ==>", "[{EMPTY_MATCH=null, ?X=p}]");
        shouldMatch("pred(a) ==>", "(pred(?)):?X:Formula ==>");

        shouldMatch("==>f(f(g(a)))= 0", "==> (f((...g((...a...):?Q)...):?G)):?Y = 0", "[{?G=f(g(a)), EMPTY_MATCH=null, ?Q=a, ?Y=f(f(g(a)))}]");


//        shouldMatch("f(f(g(a)))= 0 ==>", "f( (... g( (...a...):?Q ) ...) : ?R) : ?Y = 0 ==>",
//                "[{EMPTY_MATCH=null, ?Q=a, ?Y=f(f(g(a))), ?R=f(g(a))}]");
    }


    @Test
    public void hasToplevelComma() throws Exception {
        Assert.assertTrue(!KeyMatcherFacade.hasToplevelComma("a=b,c=d").isEmpty());
        Assert.assertFalse(KeyMatcherFacade.hasToplevelComma("f(a,b)").isEmpty());
    }


    //region: helper

    private void shouldNotMatch(String s, String s1) throws Exception{
        Assert.assertTrue(shouldMatch(s, s1).size()==0);
    }

    private void shouldMatch(String keysequent, String pattern, String exp) throws Exception {

        Matchings m = shouldMatch(keysequent, pattern);
        System.out.println("m = " + m);
        Assert.assertEquals(exp, m.toString());

    }

    private Matchings shouldMatch(String keysequent, String pattern) throws ParserException {
        Sequent seq = parseKeySeq(keysequent);
        KeyMatcherFacade.KeyMatcherFacadeBuilder builder = KeyMatcherFacade.builder().environment(keyenv);
        KeyMatcherFacade kfm = builder.sequent(seq).build();
        return kfm.matches(pattern, null);

    }

    private Matchings shouldMatchT(String keyterm, String pattern) throws ParserException {
        KeyTermMatcher ktm = new KeyTermMatcher();
        Matchings m = ktm.visit(parseKeyTerm(pattern), MatchPathFacade.createRoot(parseKeyTerm(keyterm)));
        return  m;

    }

    private void shouldMatchT(String s, String s1, String exp) throws ParserException {
        Matchings m = shouldMatchT(s, s1);
        System.out.println("m = " + m);
        Assert.assertEquals(m.toString(), exp);
    }

    public Sequent parseKeySeq(String seq) throws ParserException {
        Reader in = new StringReader(seq);
        return dtp.parseSeq(in, services, namespace, abbrev);
    }

    public Term parseKeyTerm(String t) throws ParserException {
        Reader in = new StringReader(t);
        return dtp.parse(in, null, services, namespace, abbrev, true);
    }

    //endregion


}