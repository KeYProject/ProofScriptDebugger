package edu.kit.iti.formal.psdbg.interpreter.matcher;

import de.uka.ilkd.key.control.DefaultUserInterfaceControl;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.logic.Sequent;
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


    @Test
    public void matchSeq() throws Exception {
        shouldMatch("p ==>", "p ==>");
        shouldMatch("==> pred(a), q", "==> pred(?X:S), q");
        shouldMatch("==> p, q", "==> ?X:Formula");
        shouldMatch("==> p, q", "==> p, q");
        shouldMatch("==> p, q", "==> q, p");
        shouldMatch("==> pred(a), q", "==> pred(?X)");


    }

    @Test
    public void hasToplevelComma() throws Exception {
        Assert.assertTrue(!KeyMatcherFacade.hasToplevelComma("a=b,c=d").isEmpty());
        Assert.assertFalse(KeyMatcherFacade.hasToplevelComma("f(a,b)").isEmpty());
    }


    //region: helper
    private void shouldMatch(String keysequent, String pattern) throws ParserException {
        Sequent seq = parseKeySeq(keysequent);
        KeyMatcherFacade.KeyMatcherFacadeBuilder builder = KeyMatcherFacade.builder().environment(keyenv);
        KeyMatcherFacade kfm = builder.sequent(seq).build();
        Matchings m = kfm.matches(pattern, null);
        System.out.println(m);
    }

    public Sequent parseKeySeq(String seq) throws ParserException {
        Reader in = new StringReader(seq);
        return dtp.parseSeq(in, services, namespace, abbrev);
    }

    //endregion


}