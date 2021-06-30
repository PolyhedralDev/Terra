package com.dfsek.terra.config.loaders;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.world.Flora;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Class to hold Type instances for types with generics.
 */
@SuppressWarnings("unused")
public final class Types {
    public static final Type BLOCK_DATA_PROBABILITY_COLLECTION_TYPE;
    public static final Type FLORA_PROBABILITY_COLLECTION_TYPE;
    public static final Type TREE_PROBABILITY_COLLECTION_TYPE;
    public static final Type TERRA_BIOME_PROBABILITY_COLLECTION_TYPE;
    public static final Type TERRA_BIOME_TERRA_BIOME_PROBABILITY_COLLECTION_MAP;

    static {
        BLOCK_DATA_PROBABILITY_COLLECTION_TYPE = getType("blockStateProbabilityCollection");
        FLORA_PROBABILITY_COLLECTION_TYPE = getType("floraProbabilityCollection");
        TREE_PROBABILITY_COLLECTION_TYPE = getType("treeProbabilityCollection");
        TERRA_BIOME_PROBABILITY_COLLECTION_TYPE = getType("terraBiomeProbabilityCollection");
        TERRA_BIOME_TERRA_BIOME_PROBABILITY_COLLECTION_MAP = getType("terraBiomeProbabilityCollectionMap");
    }

    private ProbabilityCollection<BlockState> blockStateProbabilityCollection;
    private ProbabilityCollection<Flora> floraProbabilityCollection;
    private ProbabilityCollection<Tree> treeProbabilityCollection;
    private ProbabilityCollection<TerraBiome> terraBiomeProbabilityCollection;
    private Map<TerraBiome, ProbabilityCollection<TerraBiome>> terraBiomeProbabilityCollectionMap;

    private static Type getType(String dummyFieldName) {
        try {
            return Types.class.getDeclaredField(dummyFieldName).getGenericType();
        } catch(NoSuchFieldException e) {
            throw new Error("this should never happen. i dont know what you did to make this happen but something is very wrong.");
        }
    }
}