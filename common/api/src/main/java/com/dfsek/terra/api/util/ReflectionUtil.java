package com.dfsek.terra.api.util;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class ReflectionUtil {
    public static Field[] getFields(@NotNull Class<?> type) {
        Field[] result = type.getDeclaredFields();
        Class<?> parentClass = type.getSuperclass();
        if(parentClass != null) {
            result = Stream.concat(Arrays.stream(result), Arrays.stream(getFields(parentClass))).toArray(Field[]::new);
        }
        return result;
    }

    public static Method[] getMethods(@NotNull Class<?> type) {
        Method[] result = type.getDeclaredMethods();
        Class<?> parentClass = type.getSuperclass();
        if(parentClass != null) {
            result = Stream.concat(Arrays.stream(result), Arrays.stream(getMethods(parentClass))).toArray(Method[]::new);
        }
        return result;
    }

    public static <T extends Annotation> void ifAnnotationPresent(AnnotatedElement element, Class<? extends T> annotation, Consumer<T> operation) {
        T a = element.getAnnotation(annotation);
        if(a != null) operation.accept(a);
    }
}
