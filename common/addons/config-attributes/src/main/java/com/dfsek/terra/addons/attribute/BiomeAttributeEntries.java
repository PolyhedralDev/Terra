package com.dfsek.terra.addons.attribute;

import java.util.List;

import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.world.attribute.AttributeMap;


record BiomeAttributeEntries(AttributeMap entries, List<String> warnings) implements Properties {
    BiomeAttributeEntries {
        warnings = List.copyOf(warnings);
    }
}
