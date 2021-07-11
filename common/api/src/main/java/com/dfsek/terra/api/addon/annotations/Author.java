package com.dfsek.terra.api.addon.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional annotation that specifies the author of an com.dfsek.terra.addon.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Author {
    /**
     * @return Name of the com.dfsek.terra.addon author.
     */
    @NotNull String value();
}
