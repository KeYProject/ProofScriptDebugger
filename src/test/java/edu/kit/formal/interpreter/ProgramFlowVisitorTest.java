package edu.kit.formal.interpreter;

import edu.kit.formal.interpreter.funchdl.DefaultLookup;
import edu.kit.formal.interpreter.graphs.ProgramFlowVisitor;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.TransformAst;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by weigl on 22.06.2017.
 */
public class ProgramFlowVisitorTest {
    @Test
    public void test() throws IOException {
        ScriptLanguageParser a = Facade.getParser(CharStreams.fromStream(getClass().getResourceAsStream("/edu/kit/formal/interpreter/simple1.txt")));
        List<ProofScript> scripts = (List<ProofScript>) a.start().accept(new TransformAst());
        ProofScript s = scripts.get(0);
        ProgramFlowVisitor pfv = new ProgramFlowVisitor(new DefaultLookup());
        s.accept(pfv);
        System.out.println(pfv.asdot());
    }

}