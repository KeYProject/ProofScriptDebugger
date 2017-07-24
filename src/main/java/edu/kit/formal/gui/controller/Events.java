package edu.kit.formal.gui.controller;

import com.google.common.eventbus.EventBus;
import de.uka.ilkd.key.rule.TacletApp;
import edu.kit.formal.gui.controls.ScriptArea;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * See http://codingjunkie.net/guava-eventbus/ for an introduction.
 *
 * Created by weigl on 7/11/17.
 */
public class Events {
    public static final EventBus GLOBAL_EVENT_BUS = new EventBus();

    public static void register(Object object) {
        GLOBAL_EVENT_BUS.register(object);
    }

    public static void unregister(Object object) {
        GLOBAL_EVENT_BUS.unregister(object);
    }

    public static void fire(Object focusScriptArea) {
        GLOBAL_EVENT_BUS.post(focusScriptArea);
    }

    @Data
    @RequiredArgsConstructor
    public static class FocusScriptArea {
        private final ScriptArea scriptArea;

    }

    @Data
    @RequiredArgsConstructor
    public static class TacletApplicationEvent {
        private final TacletApp app;
    }
}
