package com.dfsek.terra.api.event.annotations;

import com.dfsek.terra.api.event.events.PackEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that an event handler is to handle all {@link PackEvent}s, regardless of whether the pack
 * depends on the com.dfsek.terra.addon's listener.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Global {
}
