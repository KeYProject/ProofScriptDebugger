package edu.kit.formal.interpreter;

import edu.kit.formal.interpreter.funchdl.BuiltinCommands;
import edu.kit.formal.interpreter.funchdl.CommandLookup;
import edu.kit.formal.interpreter.funchdl.DefaultLookup;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ast.ProofScript;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Test Main, will be replaced
 */
public class Main {
    //This hard coded link is only for the first testing will be removed later on
    private static File testFile = new File("/home/sarah/Documents/KIT_Mitarbeiter/ProofScriptingLanguage/src/test/resources/edu/kit/formal/proofscriptparser/scripts/test.txt");

    public static void main(String[] args) {
        //Erzeuge Parser
        //lese P ein
        //Erzeuge Interpreter
        //rufe interpret(AST) auf interpreter auf
        try {
            List<ProofScript> l = Facade.getAST(testFile);
            DefaultLookup lookup = new DefaultLookup();
            lookup.getBuilders().add(
                    new BuiltinCommands.PrintCommand());
            lookup.getBuilders().add(
                    new BuiltinCommands.SplitCommand());

            Interpreter inter = new Interpreter(lookup);
            inter.interpret(l, "TestSeq");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
