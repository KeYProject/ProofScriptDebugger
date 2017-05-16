package edu.kit.formal.interpreter;

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
        Facade f = new Facade();
        try {
            List<ProofScript> l = f.getAST(testFile);
            Interpreter inter = new Interpreter();
            inter.interpret(l, "TestSeq");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
