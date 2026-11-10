package com.dfsek.terra.api.world.attribute;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.registry.key.TagKey;
import com.dfsek.terra.api.util.generic.either.Either;
import com.dfsek.terra.api.world.attribute.value.Color;
import com.dfsek.terra.api.world.attribute.value.TriState;


public interface AttributeTypes {
    /**
     * {@code and}, {@code nand}, {@code or}, {@code nor}, {@code xor}, {@code xnor}.
     */
    AttributeType<Boolean> booleanType();

    /**
     * Override only.
     */
    AttributeType<TriState> triState();

    /**
     * {@code alpha_blend}, {@code add}, {@code subtract}, {@code multiply}, {@code minimum}, {@code maximum}.
     */
    AttributeType<Float> floatType();

    /**
     * The {@link #floatType()} library.
     */
    AttributeType<Float> angleDegrees();

    /**
     * {@code alpha_blend}, {@code add}, {@code subtract}, {@code multiply}, {@code blend_to_gray}.
     */
    AttributeType<Color> rgbColor();

    /**
     * {@code alpha_blend}, {@code add}, {@code subtract}, {@code multiply}, {@code blend_to_gray}.
     */
    AttributeType<Color> argbColor();

    /**
     * {@code add}, {@code subtract}, {@code multiply}, {@code minimum}, {@code maximum}.
     */
    AttributeType<Integer> integer();

    /**
     * Override only.
     */
    AttributeType<Integer> integerOverrideOnly();

    /**
     * Any ID; a bare ID takes {@code defaultNamespace}. Tags are rejected. Override only.
     */
    AttributeType<RegistryKey> identifier(String defaultNamespace);

    /**
     * An ID or a {@code #tag}; a bare ID takes {@code defaultNamespace}. Override only.
     */
    AttributeType<Either<RegistryKey, TagKey>> identifierOrTag(String defaultNamespace);

    /**
     * An enum identified by its {@link StringIdentifiable#getID() ID}. Override only.
     *
     * @param type its constants' IDs must be distinct
     */
    <E extends Enum<E> & StringIdentifiable> AttributeType<E> enumType(String id, Class<E> type);
}
