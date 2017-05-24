package edu.kit.formal.interpreter;

import de.uka.ilkd.key.api.*;
import de.uka.ilkd.key.macros.scripts.RuleCommand;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import edu.kit.formal.interpreter.funchdl.BuiltinCommands;
import edu.kit.formal.interpreter.funchdl.DefaultLookup;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ast.ProofScript;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test Main, will be replaced
 */
public class Main {
    static ProofManagementApi api;
    //This hard coded link is only for the first testing will be removed later on
    private static File testFile = new File("/home/sarah/Documents/KIT_Mitarbeiter/ProofScriptingLanguage/src/test/resources/edu/kit/formal/proofscriptparser/scripts/test.txt");
    private static File testFile1 = new File("/home/sarah/Documents/KIT_Mitarbeiter/ProofScriptingLanguage/src/test/resources/edu/kit/formal/proofscriptparser/scripts/contraposition.key");

    public static void main(String[] args) {
        try {
            System.out.println(testFile1.exists());

            api = KeYApi.loadFromKeyFile(testFile1);
            ProofApi papi = api.getLoadedProof();
            ScriptApi scrapi = papi.getScriptApi();
            System.out.println(papi.getFirstOpenGoal().getSequent().toString());
            ProjectedNode openGoal = papi.getFirstOpenGoal();
            RuleCommand rc = (RuleCommand) KeYApi.getScriptCommandApi().getScriptCommands("rule");
            Map cArgs = new HashMap();
            VariableAssignments va = new VariableAssignments();
            cArgs.put("#2", "impRight");
            ProofScriptCommandCall impRight = scrapi.instantiateCommand(rc, cArgs);
            scrapi.executeScriptCommand(impRight, openGoal, va);
            VariableAssignments va2 = new VariableAssignments(va);
            va2.addType("X", VariableAssignments.VarType.FORMULA);
            va2.addType("Y", VariableAssignments.VarType.FORMULA);

            List<VariableAssignments> matches = scrapi.matchPattern("==> X -> Y", openGoal.getSequent(), va2);
            for (VariableAssignments match : matches) {
                System.out.println(match);
            }
            if (matches.isEmpty()) {
                System.out.println("No match found");
            } else {
                List<VariableAssignments> matches2 = scrapi.matchPattern("==> X -> Y", openGoal.getSequent(), va2);

            }
        } catch (ProblemLoaderException e) {
            System.out.println("Could not load file");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
