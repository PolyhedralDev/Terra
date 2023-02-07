package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.village.VillagerType;


public class VillagerTypeTemplate implements ObjectTemplate<VillagerType> {
    @Value("id")
    @Default
    private Identifier id = null;
    
    @Override
    public VillagerType get() {
        return Registries.VILLAGER_TYPE.get(id);
    }
}
