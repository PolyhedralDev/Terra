package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.structure.features.EntityFeature;
import com.dfsek.terra.structure.features.Feature;
import com.dfsek.terra.util.MaterialSet;
import org.bukkit.entity.EntityType;
import org.polydev.gaea.math.Range;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings({"unchecked", "SwitchStatementWithTooFewBranches"}) // We will do more features laterTM
public class StructureFeatureLoader implements TypeLoader<Feature> {
    @Override
    public Feature load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) o;
        switch((String) map.get("type")) {
            case "ENTITY_FEATURE":
                MaterialSet stand = (MaterialSet) configLoader.loadType(MaterialSet.class, map.get("spawnable-on"));
                MaterialSet in = (MaterialSet) configLoader.loadType(MaterialSet.class, map.get("spawnable-in"));
                Range amount = (Range) configLoader.loadType(Range.class, map.get("amount"));
                EntityType entityType = (EntityType) configLoader.loadType(EntityType.class, map.get("entity"));
                Integer height = (Integer) configLoader.loadType(Integer.class, map.get("in-height"));
                return new EntityFeature(entityType, amount, stand, in, height);
            default:
                throw new LoadException("Invalid feature type: \"" + map.get("type") + "\"");
        }
    }
}
