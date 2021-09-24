package com.dfsek.terra.api.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dfsek.terra.api.command.arg.ArgumentParser;
import com.dfsek.terra.api.command.arg.StringArgumentParser;
import com.dfsek.terra.api.command.tab.NothingCompleter;
import com.dfsek.terra.api.command.tab.TabCompleter;


@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {
    String value();
    
    boolean required() default true;
    
    Class<? extends TabCompleter> tabCompleter() default NothingCompleter.class;
    
    Class<? extends ArgumentParser<?>> argumentParser() default StringArgumentParser.class;
    
    String defaultValue() default "";
}
