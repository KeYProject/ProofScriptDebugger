package edu.kit.iti.formal.psdbg.gui.model;

import javax.swing.*;

public enum InterpreterThreadState {
    NO_THREAD(null),
    WAIT(null),
    RUNNING(null),
    ERROR(null),
    FINISHED(null);

    private final Icon icon;

    InterpreterThreadState(Icon icon) {
        this.icon = icon;
    }

    public Icon getIcon() {
        return icon;
    }
}
