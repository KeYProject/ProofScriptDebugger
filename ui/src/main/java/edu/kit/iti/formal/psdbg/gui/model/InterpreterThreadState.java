package edu.kit.iti.formal.psdbg.gui.model;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;

public enum InterpreterThreadState {
    NO_THREAD(MaterialDesignIcon.FLASK_EMPTY),
    WAIT(MaterialDesignIcon.CLOCK_ALERT),
    RUNNING(MaterialDesignIcon.RUN),
    ERROR(MaterialDesignIcon.EXCLAMATION),
    FINISHED(MaterialDesignIcon.CHECK);

    private final MaterialDesignIcon icon;

    InterpreterThreadState(MaterialDesignIcon icon) {
        this.icon = icon;
    }

    public MaterialDesignIcon getIcon() {
        return icon;
    }
}
