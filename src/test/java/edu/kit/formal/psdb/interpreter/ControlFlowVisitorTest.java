package edu.kit.formal.psdb.interpreter;

import edu.kit.formal.psdb.interpreter.funchdl.DefaultLookup;
import edu.kit.formal.psdb.interpreter.graphs.ControlFlowVisitor;
import edu.kit.formal.psdb.parser.Facade;
import edu.kit.formal.psdb.parser.ScriptLanguageParser;
import edu.kit.formal.psdb.parser.TransformAst;
import edu.kit.formal.psdb.parser.ast.ProofScript;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by weigl on 22.06.2017.
 */
public class ControlFlowVisitorTest {
    @Test
    public void test() throws IOException {
        ScriptLanguageParser a = Facade.getParser(CharStreams.fromStream(getClass().getResourceAsStream("/edu/kit/formal/psdb/interpreter/simple1.txt")));
        List<ProofScript> scripts = (List<ProofScript>) a.start().accept(new TransformAst());
        ProofScript s = scripts.get(0);
        ControlFlowVisitor pfv = new ControlFlowVisitor(new DefaultLookup());
        s.accept(pfv);
        System.out.println(pfv.asdot());
    }

}