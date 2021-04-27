package com.dfsek.terra.api.docs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For use in Terra AutoDoc, to specify
 * that references to the annotated class
 * should be shadowed to the target class.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface AutoDocShadow {
    String value();
}
