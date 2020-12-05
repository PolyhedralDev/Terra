package com.dfsek.terra.config.loaders;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.palette.Palette;

import java.lang.reflect.Type;
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

    static {
        MATERIAL_SET_TYPE = getType("materialSet");
        MATERIAL_PROBABILITY_COLLECTION_TYPE = getType("materialProbabilityCollection");
        BLOCK_DATA_PALETTE_TYPE = getType("blockDataPalette");
        BLOCK_DATA_PROBABILITY_COLLECTION_TYPE = getType("blockDataProbabilityCollection");
        FLORA_PROBABILITY_COLLECTION_TYPE = getType("floraProbabilityCollection");
        TREE_PROBABILITY_COLLECTION_TYPE = getType("treeProbabilityCollection");
    }

    private Set<Material> materialSet;
    private Palette<BlockData> blockDataPalette;
    private ProbabilityCollection<Material> materialProbabilityCollection;
    private ProbabilityCollection<BlockData> blockDataProbabilityCollection;
    private ProbabilityCollection<Flora> floraProbabilityCollection;
    private ProbabilityCollection<Tree> treeProbabilityCollection;

    private static Type getType(String dummyFieldName) {
        try {
            return Types.class.getDeclaredField(dummyFieldName).getGenericType();
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}
