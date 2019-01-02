package edu.kit.iti.formal.psdbg.gui.actions.inline;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.swing.*;


@NoArgsConstructor
public abstract class InlineAction extends AbstractAction implements Comparable<InlineAction> {
    @Getter
    private int priority;

    public InlineAction(String name, int priority, Icon graphics) {
        super(name, graphics);
        this.priority = priority;
    }

    @Override
    public int compareTo(InlineAction o) {
        return priority - o.priority;
    }
}