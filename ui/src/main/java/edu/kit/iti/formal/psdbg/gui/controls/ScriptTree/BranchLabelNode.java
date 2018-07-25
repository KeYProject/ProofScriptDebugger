package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import de.uka.ilkd.key.proof.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BranchLabelNode extends AbstractTreeNode {
    @Getter
    private final Node keyBranchNode;

    @Getter
    private final String labelName;

}
