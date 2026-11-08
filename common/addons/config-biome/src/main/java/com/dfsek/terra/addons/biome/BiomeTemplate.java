/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome;

import com.dfsek.tectonic.api.config.template.ValidatedConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Final;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.exception.ValidationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.world.biome.PlatformBiome;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class BiomeTemplate implements AbstractableTemplate, ValidatedConfigTemplate {
    private final ConfigPack pack;

    @Value("id")
    @Final
    private @Meta String id;

    @Value("extends")
    @Final
    @Default
    private List<String> extended = Collections.emptyList();

    @Value("vanilla")
    private @Meta PlatformBiome vanilla;

    @Value("color")
    @Final
    @Default
    private @Meta int color = 0;
    @Value("tags")
    @Default
    private @Meta Set<@Meta String> tags = new HashSet<>();

    private Set<RegistryKey> platformTags = Set.of();

    public BiomeTemplate(ConfigPack pack, Platform platform) {
        this.pack = pack;
    }

    @Override
    public boolean validate() throws ValidationException {
        color |= 0xff000000; // Alpha adjustment
        Set<RegistryKey> parsed = new HashSet<>();
        for(String tag : tags) {
            int colon = tag.indexOf(':');
            if(colon < 0) continue;
            // RegistryKey.parse accepts an empty namespace or ID.
            if(colon == 0) throw new ValidationException("Invalid namespaced biome tag \"" + tag + "\": the namespace is empty");
            if(colon == tag.length() - 1) throw new ValidationException("Invalid namespaced biome tag \"" + tag + "\": the ID is empty");
            try {
                parsed.add(RegistryKey.parse(tag));
            } catch(IllegalArgumentException e) {
                throw new ValidationException("Invalid namespaced biome tag \"" + tag + "\": " + e.getMessage());
            }
        }
        platformTags = Set.copyOf(parsed);
        return true;
    }

    public List<String> getExtended() {
        return extended;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Set<RegistryKey> getPlatformTags() {
        return platformTags;
    }

    public int getColor() {
        return color;
    }

    public ConfigPack getPack() {
        return pack;
    }

    public String getID() {
        return id;
    }

    public PlatformBiome getVanilla() {
        return vanilla;
    }
}
