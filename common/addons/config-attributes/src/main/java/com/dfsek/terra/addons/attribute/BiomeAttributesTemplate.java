package com.dfsek.terra.addons.attribute;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Merge;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.Collections;
import java.util.Map;

import com.dfsek.terra.api.config.meta.Meta;


public class BiomeAttributesTemplate implements ConfigTemplate {
    @Value("attributes")
    @Merge
    @Default
    private @Meta Map<@Meta String, LazyConfigValue> attributes = Map.of();

    Map<String, LazyConfigValue> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}
