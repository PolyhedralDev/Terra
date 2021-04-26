package com.dfsek.terra.api.docs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For use in Terra AutoDoc, to specify
 * that references to the annotated class
 * should be refactored in the documentation.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface AutoDocAlias {
    String value();
}
