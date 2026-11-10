package com.dfsek.terra.api.world.attribute;

@FunctionalInterface
public interface AttributeModifier<T, A> {
    T apply(T subject, A argument);
}
