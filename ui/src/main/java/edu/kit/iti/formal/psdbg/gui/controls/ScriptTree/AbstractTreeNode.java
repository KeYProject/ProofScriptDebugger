package edu.kit.iti.formal.psdbg.gui.controls.ScriptTree;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AbstractTreeNode {
    @Getter @Setter
    private AbstractTreeNode parent;

    @Getter @Setter
    private List<AbstractTreeNode> children;

}
