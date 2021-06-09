package com.dfsek.terra.api.config.meta;

/**
 * Context from which to pull {@link MetaValue}s
 */
public interface MetaContext {
    <T> T load(Object metaCandidate);
}
