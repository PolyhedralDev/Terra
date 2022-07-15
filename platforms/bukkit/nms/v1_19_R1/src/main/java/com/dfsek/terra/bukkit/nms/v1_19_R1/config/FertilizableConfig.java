package com.dfsek.terra.bukkit.nms.v1_19_R1.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class FertilizableConfig implements ObjectTemplate<FertilizableConfig> {
    @Value("strucutres")
    @Default
    private ProbabilityCollection<ConfiguredStructure> structures = null;
    
    @Value("cooldowns")
    @Default
    private Map<ResourceLocation, Double> cooldowns = null;
    
    @Value("can-grow")
    @Default
    private ConfiguredStructure canGrow = null;
    
    @Value("villager-fertilizable")
    @Default
    private Boolean villagerFertilizable = null;
    
    public ProbabilityCollection<ConfiguredStructure> getStructures() {
        return structures;
    }
    
    public Map<ResourceLocation, Double> getCooldowns() {
        return cooldowns;
    }
    
    public ConfiguredStructure getCanGrow() {
        return canGrow;
    }
    
    public Boolean isVillagerFertilizable() {
        return villagerFertilizable;
    }
    
    @Override
    public FertilizableConfig get() {
        return this;
    }
}
