package edu.kit.formal.gui.controller;

import com.google.common.eventbus.EventBus;

/**
 * See http://codingjunkie.net/guava-eventbus/ for an introduction.
 *
 * Created by weigl on 7/11/17.
 */
public class Events {
    public static final EventBus GLOBAL_EVENT_BUS = new EventBus();

    public void register(Object object) {
        GLOBAL_EVENT_BUS.register(object);
    }

    public void unregister(Object object) {
        GLOBAL_EVENT_BUS.unregister(object);
    }
}
