package edu.kit.formal.gui.model;

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
    private final File file;
    private final int lineNumber;
}
