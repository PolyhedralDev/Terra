package com.dfsek.terra.config.loaders;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.world.flora.Flora;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.api.world.tree.Tree;
import com.dfsek.terra.biome.TerraBiome;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * Class to hold Type instances for types with generics.
 */
@SuppressWarnings("unused")
public final class Types {
    public static final Type MATERIAL_SET_TYPE;
    public static final Type MATERIAL_PROBABILITY_COLLECTION_TYPE;
    public static final Type BLOCK_DATA_PALETTE_TYPE;
    public static final Type BLOCK_DATA_PROBABILITY_COLLECTION_TYPE;
    public static final Type FLORA_PROBABILITY_COLLECTION_TYPE;
    public static final Type TREE_PROBABILITY_COLLECTION_TYPE;
    public static final Type TERRA_BIOME_PROBABILITY_COLLECTION_TYPE;
    public static final Type TERRA_BIOME_TERRA_BIOME_PROBABILITY_COLLECTION_MAP;

    static {
        MATERIAL_SET_TYPE = getType("materialSet");
        MATERIAL_PROBABILITY_COLLECTION_TYPE = getType("materialProbabilityCollection");
        BLOCK_DATA_PALETTE_TYPE = getType("blockDataPalette");
        BLOCK_DATA_PROBABILITY_COLLECTION_TYPE = getType("blockDataProbabilityCollection");
        FLORA_PROBABILITY_COLLECTION_TYPE = getType("floraProbabilityCollection");
        TREE_PROBABILITY_COLLECTION_TYPE = getType("treeProbabilityCollection");
        TERRA_BIOME_PROBABILITY_COLLECTION_TYPE = getType("terraBiomeProbabilityCollection");
        TERRA_BIOME_TERRA_BIOME_PROBABILITY_COLLECTION_MAP = getType("terraBiomeProbabilityCollectionMap");
    }

    private Set<MaterialData> materialSet;
    private Palette<BlockData> blockDataPalette;
    private ProbabilityCollection<MaterialData> materialProbabilityCollection;
    private ProbabilityCollection<BlockData> blockDataProbabilityCollection;
    private ProbabilityCollection<Flora> floraProbabilityCollection;
    private ProbabilityCollection<Tree> treeProbabilityCollection;
    private ProbabilityCollection<TerraBiome> terraBiomeProbabilityCollection;
    private Map<TerraBiome, ProbabilityCollection<TerraBiome>> terraBiomeProbabilityCollectionMap;

    private static Type getType(String dummyFieldName) {
        try {
            return Types.class.getDeclaredField(dummyFieldName).getGenericType();
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}
