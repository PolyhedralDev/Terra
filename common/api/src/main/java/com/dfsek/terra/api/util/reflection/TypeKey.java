/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.reflection;

import com.dfsek.tectonic.util.ClassAnnotatedTypeImpl;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public class TypeKey<T> {
    final Class<? super T> rawType;
    final Type type;
    
    final AnnotatedType annotatedType;
    final int hashCode;
    
    @SuppressWarnings("unchecked")
    protected TypeKey() {
        this.type = getSuperclassTypeParameter(getClass());
        this.annotatedType = getAnnotatedSuperclassTypeParameter(getClass());
        this.rawType = (Class<? super T>) ReflectionUtil.getRawType(type);
        this.hashCode = type.hashCode();
    }
    
    protected TypeKey(Class<T> clazz) {
        this.type = clazz;
        this.rawType = clazz;
        this.annotatedType = new ClassAnnotatedTypeImpl(clazz);
        this.hashCode = type.hashCode();
    }
    
    public static <T> TypeKey<T> of(Class<T> clazz) {
        return new TypeKey<>(clazz);
    }
    
    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if(superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }
    
    private static AnnotatedType getAnnotatedSuperclassTypeParameter(Class<?> subclass) {
        AnnotatedType superclass = subclass.getAnnotatedSuperclass();
        if(superclass.getType() instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        AnnotatedParameterizedType parameterized = (AnnotatedParameterizedType) superclass;
        return parameterized.getAnnotatedActualTypeArguments()[0];
    }
    
    /**
     * Returns the raw (non-generic) type for this type.
     */
    public final Class<? super T> getRawType() {
        return rawType;
    }
    
    /**
     * Gets underlying {@code Type} instance.
     */
    public final Type getType() {
        return type;
    }
    
    public final AnnotatedType getAnnotatedType() {
        return annotatedType;
    }
    
    @Override
    public final int hashCode() {
        return this.hashCode;
    }
    
    @Override
    public final boolean equals(Object o) {
        return o instanceof TypeKey<?>
               && ReflectionUtil.equals(type, ((TypeKey<?>) o).type);
    }
    
    @Override
    public final String toString() {
        return ReflectionUtil.typeToString(type);
    }
}

