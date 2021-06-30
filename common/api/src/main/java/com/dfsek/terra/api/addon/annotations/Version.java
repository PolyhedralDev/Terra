package com.dfsek.terra.api.addon.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional annotation that specifies the version of an com.dfsek.terra.addon.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Version {
    /**
     * @return Version of the com.dfsek.terra.addon.
     */
    @NotNull String value();
}
