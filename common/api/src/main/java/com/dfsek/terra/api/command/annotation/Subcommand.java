package com.dfsek.terra.api.command.annotation;

import com.dfsek.terra.api.command.CommandTemplate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Subcommand {
    String value();

    String[] aliases() default {};

    Class<? extends CommandTemplate> clazz();
}
