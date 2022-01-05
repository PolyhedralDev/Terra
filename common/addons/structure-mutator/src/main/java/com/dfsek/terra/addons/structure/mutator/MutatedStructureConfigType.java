package com.dfsek.terra.addons.structure.mutator;

import com.dfsek.terra.addons.structure.mutator.config.MutatedStructureTemplate;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class MutatedStructureConfigType implements ConfigType<MutatedStructureTemplate, MutatedStructure> {
    private final BaseAddon addon;
    
    public MutatedStructureConfigType(BaseAddon addon) {
        this.addon = addon;
    }
    
    @Override
    public MutatedStructureTemplate getTemplate(ConfigPack pack, Platform platform) {
        return new MutatedStructureTemplate();
    }
    
    @Override
    public ConfigFactory<MutatedStructureTemplate, MutatedStructure> getFactory() {
        return new MutatedStructureFactory(addon);
    }
    
    @Override
    public TypeKey<MutatedStructure> getTypeKey() {
        return TypeKey.of(MutatedStructure.class);
    }
}
