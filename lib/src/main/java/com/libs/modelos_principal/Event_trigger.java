package com.libs.modelos_principal;

import com.libs.runnable.custom_runnable;

public class Event_trigger {
    public String name;
    public custom_runnable function;

    public Event_trigger(String name, custom_runnable function) {
        this.name = name;
        this.function = function;
    }
}
