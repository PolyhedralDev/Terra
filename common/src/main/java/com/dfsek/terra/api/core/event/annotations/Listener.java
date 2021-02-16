package com.dfsek.terra.api.core.event.annotations;

public @interface Listener {
    int priority() default 0;
}
