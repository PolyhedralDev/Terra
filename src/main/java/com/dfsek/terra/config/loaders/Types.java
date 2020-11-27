package com.dfsek.terra.config.loaders;

import org.bukkit.Material;
import org.polydev.gaea.math.ProbabilityCollection;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Class to hold Type instances for types with generics.
 */
@SuppressWarnings("unused")
public final class Types {
    public static final Type MATERIAL_SET_TYPE;
    public static final Type MATERIAL_PROBABILITY_COLLECTION_TYPE;

    static {
        MATERIAL_SET_TYPE = getType("materialSet");
        MATERIAL_PROBABILITY_COLLECTION_TYPE = getType("materialProbabilityCollection");
    }

    private Set<Material> materialSet;
    private ProbabilityCollection<Material> materialProbabilityCollection;

    private static Type getType(String dummyFieldName) {
        try {
            return Types.class.getDeclaredField(dummyFieldName).getGenericType();
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}
