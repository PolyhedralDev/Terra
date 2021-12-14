/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.reflection;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Objects;


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
    
    public static boolean equals(Type a, Type b) {
        if(a == b) {
            return true;
        } else if(a instanceof Class) {
            return a.equals(b);
        } else if(a instanceof ParameterizedType pa) {
            if(!(b instanceof ParameterizedType pb)) {
                return false;
            }
            
            return Objects.equals(pa.getOwnerType(), pb.getOwnerType())
                   && pa.getRawType().equals(pb.getRawType())
                   && Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments());
        } else if(a instanceof GenericArrayType ga) {
            if(!(b instanceof GenericArrayType gb)) {
                return false;
            }
            
            return equals(ga.getGenericComponentType(), gb.getGenericComponentType());
        } else if(a instanceof WildcardType wa) {
            if(!(b instanceof WildcardType wb)) {
                return false;
            }
            
            return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds())
                   && Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());
        } else if(a instanceof TypeVariable<?> va) {
            if(!(b instanceof TypeVariable<?> vb)) {
                return false;
            }
            return va.getGenericDeclaration() == vb.getGenericDeclaration()
                   && va.getName().equals(vb.getName());
        } else {
            return false;
        }
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
    
    public AnnotatedType getAnnotatedType() {
        return annotatedType;
    }
    
    @Override
    public final int hashCode() {
        return this.hashCode;
    }
    
    @Override
    public final boolean equals(Object o) {
        return o instanceof TypeKey<?>
               && equals(type, ((TypeKey<?>) o).type);
    }
    
    @Override
    public final String toString() {
        return ReflectionUtil.typeToString(type);
    }
}

