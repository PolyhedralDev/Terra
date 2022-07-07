package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerType;


public class VillagerTypeTemplate implements ObjectTemplate<VillagerType> {
    @Value("id")
    @Default
    private Identifier id = null;
    
    @Override
    public VillagerType get() {
        return Registry.VILLAGER_TYPE.get(id);
    }
}
