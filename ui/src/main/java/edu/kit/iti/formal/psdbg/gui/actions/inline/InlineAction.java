package edu.kit.iti.formal.psdbg.gui.actions.inline;

import javafx.beans.DefaultProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InlineAction implements Comparable<InlineAction> {
    private String name;
    private int priority;
    private Node graphics;
    private EventHandler<ActionEvent> eventHandler;

    @Override
    public int compareTo(InlineAction o) {
        return priority - o.priority;
    }
}