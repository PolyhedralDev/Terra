package com.dfsek.terra.api.injection;

import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.injection.exception.InjectionException;
import com.dfsek.terra.api.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class Injector<T> {
    private final T value;
    private final Set<Class<? extends T>> targets = new HashSet<>();

    public Injector(T value) {
        this.value = value;
    }

    public void addExplicitTarget(Class<? extends T> target) {
        targets.add(target);
    }

    public void inject(Object object) throws InjectionException {
        for(Field field : ReflectionUtil.getFields(object.getClass())) {
            Inject inject = field.getAnnotation(Inject.class);
            if(inject == null) continue;

            System.out.println(field);
            System.out.println("attempting to inject " + value.getClass() + " to " + field.getClass());
            if(value.getClass().equals(field.getType()) || targets.contains(field.getType())) {
                System.out.println("injecting...");
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
