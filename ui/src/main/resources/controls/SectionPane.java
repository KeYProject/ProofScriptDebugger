package edu.kit.iti.formal.psdbg.gui.controls;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

/**
 * @author Alexander Weigl
 * @version 1 (05.06.17)
 */
public class SectionPane extends JPanel {
    @Getter
    @Setter
    private String title;

    private JLabel titleLabel;
    private Box northBox = new Box(BoxLayout.Y_AXIS);
    private Box buttons = new Box(BoxLayout.X_AXIS);

    public SectionPane(String title) {
        super();
        //TODO swing
        titleLabel = new JLabel(title);
    }

    public SectionPane(String title, JComponent center) {
        this(title);
        add(center);
    }
}
