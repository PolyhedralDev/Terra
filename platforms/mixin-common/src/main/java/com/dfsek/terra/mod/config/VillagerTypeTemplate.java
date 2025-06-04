package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerType;


public class VillagerTypeTemplate implements ObjectTemplate<RegistryKey<VillagerType>> {
    @Value("id")
    @Default
    private String id = null;

    @Override
    public RegistryKey<VillagerType> get() {
        return RegistryKey.of(RegistryKeys.VILLAGER_TYPE, Identifier.ofVanilla(id));
    }
}
