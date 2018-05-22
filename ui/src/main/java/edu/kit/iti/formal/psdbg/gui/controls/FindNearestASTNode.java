package edu.kit.iti.formal.psdbg.gui.controls;

import edu.kit.iti.formal.psdbg.parser.ast.ASTNode;
import edu.kit.iti.formal.psdbg.parser.ast.ProofScript;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Alexander Weigl
 * @version 1 (12.03.18)
 */
@RequiredArgsConstructor
public class FindNearestASTNode {
    private final int pos;

    public ASTNode childOrMe(ASTNode me) {
        // range check
        if (me == null) {
            return null;
        }

        try {
            int start = me.getStartPosition().getOffset();
            int stop = me.getEndPosition().getOffset();
            if (start <= pos && pos <= stop) {
                for (ASTNode child : me.getChildren()) {
                    ASTNode cand = childOrMe(child);
                    if (cand != null) return cand;
                }
                return me;
            }
        } catch (NullPointerException e) {
            System.err.println("No rule context set for instance " + me);
            e.printStackTrace();
        }
        return null;
    }

    public Optional<ASTNode> find(List<ProofScript> ast) {
        return ast.stream().map(this::childOrMe)
                .filter(Objects::nonNull)
                .findFirst();
    }
}
