package com.dfsek.terra.api.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    Argument[] arguments() default {};

    Flag[] flags() default {};

    Subcommand[] subcommands() default {};

    String usage() default "";
}
