package com.dfsek.terra.api.injection;

import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.injection.exception.InjectionException;
import com.dfsek.terra.api.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * Dynamic dependency injector.
 * <p>
 * Stores an object to inject, and injects it into objects passed to {@link #inject(Object)}.
 *
 * @param <T> Type of object to inject.
 */
public class Injector<T> {
    private final T value;
    private final Set<Class<? extends T>> targets = new HashSet<>();

    /**
     * Instantiate an Injector with a value to inject
     *
     * @param value Value to inject
     */
    public Injector(T value) {
        this.value = value;
    }

    /**
     * Add an explicit class as a target. Useful for applications where subclasses may cause issues with DI.
     *
     * @param target Target class type.
     */
    public void addExplicitTarget(Class<? extends T> target) {
        targets.add(target);
    }

    /**
     * Inject the stored object into an object.
     * <p>
     * Injects the stored object into any non-static, non-final fields
     * annotated with {@link Inject},
     * with type matching the stored object or any explicit targets
     * ({@link #addExplicitTarget(Class)}.
     *
     * @param object Object to inject into
     * @throws InjectionException If:
     *                            <ul>
     *                                <li>Matching field annotated with {@link Inject} is final</li>
     *                                <li>Matching field annotated with {@link Inject} is static</li>
     *                                <li>A reflective access exception occurs</li>
     *                            </ul>
     */
    public void inject(Object object) throws InjectionException {
        for(Field field : ReflectionUtil.getFields(object.getClass())) {
            Inject inject = field.getAnnotation(Inject.class);
            if(inject == null) continue;
            if(value.getClass().equals(field.getType()) || targets.contains(field.getType())) {
                int mod = field.getModifiers();
                if(Modifier.isFinal(mod)) {
                    throw new InjectionException("Attempted to inject final field: " + field);
                }
                if(Modifier.isStatic(mod)) {
                    throw new InjectionException("Attempted to inject static field: " + field);
                }
                field.setAccessible(true);
                try {
                    field.set(object, value);
                } catch(IllegalAccessException e) {
                    throw new InjectionException("Failed to inject field: " + field, e);
                }
            }
        }
    }
}
