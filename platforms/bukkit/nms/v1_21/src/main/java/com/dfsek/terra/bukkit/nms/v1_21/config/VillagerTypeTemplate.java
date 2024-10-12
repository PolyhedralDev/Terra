package com.dfsek.terra.bukkit.nms.v1_21.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerType;


public class VillagerTypeTemplate implements ObjectTemplate<VillagerType> {
    @Value("id")
    @Default
    private ResourceLocation id = null;
    
    @Override
    public VillagerType get() {
        return BuiltInRegistries.VILLAGER_TYPE.get(id);
    }
}
