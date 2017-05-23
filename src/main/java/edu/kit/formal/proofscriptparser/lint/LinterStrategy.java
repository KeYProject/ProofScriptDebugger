package edu.kit.formal.proofscriptparser.lint;

import edu.kit.formal.proofscriptparser.ast.ProofScript;
import lombok.Getter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Alexander Weigl
 * @version 1 (23.05.17)
 */
public class LinterStrategy {
    @Getter
    private List<LintRule> checkers = new ArrayList<>();

    public List<LintProblem> check(List<ProofScript> nodes) {
        List<LintProblem> problems = new ArrayList<>(1024);
        for (LintRule checker : checkers) {
            checker.check(nodes, problems);
        }
        return problems;
    }

    public static LinterStrategy getDefaultLinter() {
        LinterStrategy ls = new LinterStrategy();
        ServiceLoader<LintRule> loader = ServiceLoader.load(LintRule.class);
        loader.forEach(ls.checkers::add);
        return ls;
    }
}
