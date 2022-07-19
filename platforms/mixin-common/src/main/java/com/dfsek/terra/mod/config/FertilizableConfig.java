package com.dfsek.terra.mod.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import net.minecraft.util.Identifier;

import java.util.Map;

import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class FertilizableConfig implements ObjectTemplate<FertilizableConfig> {
    @Value("strucutres")
    @Default
    private ProbabilityCollection<Structure> structures = null;
    
    @Value("cooldowns")
    @Default
    private Map<Identifier, Double> cooldowns = null;
    
    @Value("can-grow")
    @Default
    private Structure canGrow = null;
    
    @Value("villager-farmable")
    @Default
    private Boolean villagerFarmable = null;
    
    public ProbabilityCollection<Structure> getStructures() {
        return structures;
    }
    
    public Map<Identifier, Double> getCooldowns() {
        return cooldowns;
    }
    
    public Structure getCanGrow() {
        return canGrow;
    }
    
    public Boolean isVillagerFarmable() {
        return villagerFarmable;
    }
    
    @Override
    public FertilizableConfig get() {
        return this;
    }
}
