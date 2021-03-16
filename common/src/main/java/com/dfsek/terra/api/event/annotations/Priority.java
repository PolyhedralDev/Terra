package com.dfsek.terra.api.event.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated listener methods will have a specific priority set.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Priority {
    /**
     * Highest possible priority. Listeners with this priority will always be invoked last.
     */
    int HIGHEST = Integer.MAX_VALUE;
    /**
     * Lowest possible priority. Listeners with this priority will always be invoked first.
     */
    int LOWEST = Integer.MIN_VALUE;
    /**
     * Default priority.
     */
    int NORMAL = 0;
    /**
     * High priority.
     */
    int HIGH = 1;
    /**
     * Low Priority.
     */
    int LOW = -1;
    /**
     * @return Priority of this event. Events are executed from lowest to highest priorities.
     */
    int value();
}
