package edu.kit.formal.interpreter.funchdl;

import edu.kit.formal.interpreter.Interpreter;
import edu.kit.formal.interpreter.data.VariableAssignment;
import edu.kit.formal.proofscriptparser.Facade;
import edu.kit.formal.proofscriptparser.ast.CallStatement;
import edu.kit.formal.proofscriptparser.ast.ProofScript;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
@RequiredArgsConstructor
public class ProofScriptHandler implements CommandHandler {
    private static final String SUFFIX = ".kps";
    @Getter
    private final Map<String, ProofScript> scripts;

    @Getter
    private final List<File> searchPath = new ArrayList<>();

    public ProofScriptHandler() {
        scripts = new HashMap<>();
    }

    public ProofScriptHandler(List<ProofScript> proofScripts) {
        scripts = new HashMap<>();
        proofScripts.forEach(s -> scripts.put(s.getName(), s));
    }

    /**
     * lib/test.test
     *
     * @param name
     * @return
     */
    public ProofScript find(String name) throws IOException {
        File f = new File(name.replace('\\', '/'));
        String path = f.getParent();
        String[] filename = f.getName().split("\\.", 1);

        String candidate = path + '/' + filename[0] + SUFFIX;

        for (File dir : searchPath) {
            File test = new File(dir, candidate);
            if (test.exists()) {
                addFile(path, filename[0], test);
                return scripts.get(name);
            }
        }
        return null;
    }

    private void addFile(String path, String filename, File test) throws IOException {
        List<ProofScript> ps = Facade.getAST(test);
        for (ProofScript s : ps) {
            String name = s.getName();
            String prefix = path.replace('/', '\\') + '\\';
            if (name.equals(filename)) {
                scripts.put(prefix + name, s);
            }
            scripts.put(prefix + filename + '.' + name, s);
        }
    }

    @Override
    public boolean handles(CallStatement call) throws IllegalArgumentException {
        if (scripts.containsKey(call.getCommand()))
            return true;
        try {
            return find(call.getCommand()) != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void evaluate(Interpreter interpreter, CallStatement call, VariableAssignment params) {
        ProofScript ps = scripts.get(call.getCommand());
        //new State
        //
        //TODO create new context/introduce signature
        ps.accept(interpreter);
    }

    public void addScripts(List<ProofScript> ast) {
        ast.forEach(script -> scripts.put(script.getName(), script));
    }
}
