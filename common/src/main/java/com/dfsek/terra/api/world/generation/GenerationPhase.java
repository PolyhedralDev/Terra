package com.dfsek.terra.api.world.generation;

/**
 * The phase of terrain generation. Used for modifying values based on the phase of generation.
 */
public enum GenerationPhase {
    BASE, POPULATE, GENERATION_POPULATE, PALETTE_APPLY, POST_GEN
}
