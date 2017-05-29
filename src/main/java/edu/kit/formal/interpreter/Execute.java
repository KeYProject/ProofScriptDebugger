package edu.kit.formal.interpreter;

import de.uka.ilkd.key.api.KeYApi;
import de.uka.ilkd.key.api.ProjectedNode;
import de.uka.ilkd.key.api.ProofApi;
import de.uka.ilkd.key.api.ProofManagementApi;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import edu.kit.formal.interpreter.data.GoalNode;
import edu.kit.formal.interpreter.data.KeyData;
import edu.kit.formal.proofscriptparser.Facade;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (28.05.17)
 */
public class Execute {
    private InterpreterBuilder interpreterBuilder = new InterpreterBuilder();
    private List<String> keyFiles;
    private File scriptFile;

    public static void main(String[] args) throws IOException, ParseException {
        Execute execute = new Execute();
        execute.init(args);
        execute.run();
    }

    public Interpreter<KeyData> run() {
        try {
            ProofManagementApi pma = KeYApi.loadFromKeyFile(new File(keyFiles.get(0)));
            ProofApi pa = pma.getLoadedProof();
            ProjectedNode root = pa.getFirstOpenGoal();
            interpreterBuilder.proof(pa).macros().scriptCommands();
            pa.getProof().getProofIndependentSettings().getGeneralSettings().setOneStepSimplification(false);
            Interpreter<KeyData> inter = interpreterBuilder.build();
            KeyData keyData = new KeyData(root.getProofNode(), pa.getEnv(), pa.getProof());
            inter.interpret(Facade.getAST(scriptFile), new GoalNode<>(null, keyData));
            return inter;
        } catch (ProblemLoaderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void init(String[] args) throws ParseException, IOException {
        Options o = argparse();
        DefaultParser parser = new DefaultParser();
        CommandLine cli = parser.parse(o, args);
        keyFiles = cli.getArgList();
        scriptFile = new File(cli.getOptionValue("s"));
        if (cli.getOptionValue('p') != null)
            interpreterBuilder.scriptSearchPath(new File(cli.getOptionValue('p')));
    }


    public static Options argparse() {
        Options options = new Options();
        options.addOption("h", "--help", false, "print help text");
        options.addOption("p", "--script-path", true, "include folder for scripts");
        options.addOption("l", "--linter", false, "run linter before execute");
        options.addOption("s", "--script", true, "script file");
        return options;
    }
}
