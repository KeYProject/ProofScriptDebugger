package edu.kit.formal.interpreter;

import edu.kit.formal.proofscriptparser.DefaultASTVisitor;
import edu.kit.formal.proofscriptparser.ast.ASTNode;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Logger;

/**
 * @author Alexander Weigl
 * @version 1 (21.05.17)
 */
public class ScopeLogger extends DefaultASTVisitor<Void> {
    private Logger logger;

    @Getter
    @Setter
    private String suffix = "", prefix = "";

    public ScopeLogger(String name) {
        logger = Logger.getLogger(name);
    }

    @Override
    public Void defaultVisit(ASTNode node) {
        logger.info(suffix + node.getNodeName() + " @" + node.getStartPosition().getCharInLine() + suffix);
        logger.info(node.toString());
        return null;
    }
}
