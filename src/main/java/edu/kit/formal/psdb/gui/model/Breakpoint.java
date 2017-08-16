package edu.kit.formal.psdb.gui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.File;

/**
 * @author Alexander Weigl
 * @version 1 (21.06.17)
 */
@Data
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Breakpoint {
    private File file;
    private int lineNumber;
    private boolean enabled;
    private String condition;

    public Breakpoint(File file, int lineNumber) {
        this.file = file;
        this.lineNumber = lineNumber;
    }

    public boolean isConditional() {
        return condition != null;
    }
}
