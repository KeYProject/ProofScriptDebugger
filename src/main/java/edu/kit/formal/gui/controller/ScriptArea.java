package edu.kit.formal.gui.controller;

import edu.kit.formal.gui.model.RootModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import lombok.Getter;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

/**
 * Created by sarah on 5/27/17.
 */
public class ScriptArea extends CodeArea {
    @Getter
    private RootModel rootModel;

    public void setRootModel(RootModel rootModel) {
        this.rootModel = rootModel;

        ObservableValue<String> stringObservableValue = this.textProperty();
        SimpleObjectProperty<String> stringSimpleObjectProperty = this.rootModel.currentScriptProperty();
        stringSimpleObjectProperty.bind(stringObservableValue);
        //  rootModel.currentScriptProperty().addListener((prop, old, cur) -> {clear(); insertText(0, cur););});
    }

    public void init() {
        this.setWrapText(true);
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));

        this.setDefaultText();
    }

    private void setDefaultText() {
        this.appendText("script test(){\n\n}");
    }


}
