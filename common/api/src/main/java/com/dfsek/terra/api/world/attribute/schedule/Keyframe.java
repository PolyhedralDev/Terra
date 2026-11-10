package com.dfsek.terra.api.world.attribute.schedule;

/**
 * {@code argument} comes from {@link com.dfsek.terra.api.world.attribute.AttributeType#decodeArgument}.
 */
public record Keyframe(int ticks, Object argument) { }
