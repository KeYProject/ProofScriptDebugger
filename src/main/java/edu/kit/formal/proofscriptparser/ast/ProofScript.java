package edu.kit.formal.proofscriptparser.ast;

import edu.kit.formal.proofscriptparser.ScriptLanguageParser;
import edu.kit.formal.proofscriptparser.Visitor;
import lombok.Data;
import lombok.NonNull;

/**
 * @author Alexander Weigl
 * @version 1 (27.04.17)
 */
@Data
public class ProofScript extends ASTNode<ScriptLanguageParser.StartContext> {
    @NonNull private String name = "_";
    private Signature signature = new Signature();
    private Statements body = new Statements();

    @Override public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public ProofScript clone() {
        ProofScript ps = new ProofScript();
        ps.setName(getName());
        ps.setBody(body.clone());
        ps.setSignature(signature.clone());
        return ps;
    }


}
