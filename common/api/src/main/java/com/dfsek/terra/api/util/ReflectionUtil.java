package com.dfsek.terra.api.util;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
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

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType)type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + className);
        }
    }

    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
    }
}
