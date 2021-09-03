package com.dfsek.terra.api.injection;

import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.injection.exception.InjectionException;


/**
 * Dynamic dependency injector.
 * <p>
 * Stores an object to inject, and injects it into objects passed to {@link #inject(Object)}.
 *
 * @param <T> Type of object to inject.
 */
public interface Injector<T> {
    /**
     * Add an explicit class as a target. Useful for applications where subclasses may cause issues with DI.
     *
     * @param target Target class type.
     */
    void addExplicitTarget(Class<? extends T> target);
    
    /**
     * Inject the stored object into an object.
     * <p>
     * Injects the stored object into any non-static, non-final fields
     * annotated with {@link Inject},
     * with type matching the stored object or any explicit targets
     * ({@link #addExplicitTarget(Class)}.
     *
     * @param object Object to inject into
     *
     * @throws InjectionException If:
     *                            <ul>
     *                                <li>Matching field annotated with {@link Inject} is final</li>
     *                                <li>Matching field annotated with {@link Inject} is static</li>
     *                                <li>A reflective access exception occurs</li>
     *                            </ul>
     */
    void inject(Object object) throws InjectionException;
}
