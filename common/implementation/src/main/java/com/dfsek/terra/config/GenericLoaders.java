package com.dfsek.terra.config;

import ca.solostudios.strata.version.VersionRange;
import com.dfsek.tectonic.loading.TypeRegistry;

import java.util.LinkedHashMap;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.tectonic.LoaderRegistrar;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.config.loaders.LinkedHashMapLoader;
import com.dfsek.terra.config.loaders.MaterialSetLoader;
import com.dfsek.terra.config.loaders.ProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.RangeLoader;
import com.dfsek.terra.config.loaders.VersionRangeLoader;


public class GenericLoaders implements LoaderRegistrar {
    private final Platform platform;
    
    public GenericLoaders(Platform platform) {
        this.platform = platform;
    }
    
    @Override
    public void register(TypeRegistry registry) {
        registry.registerLoader(ProbabilityCollection.class, new ProbabilityCollectionLoader())
                .registerLoader(Range.class, new RangeLoader())
                .registerLoader(MaterialSet.class, new MaterialSetLoader())
                .registerLoader(VersionRange.class, new VersionRangeLoader())
                .registerLoader(LinkedHashMap.class, new LinkedHashMapLoader());
        
        if(platform != null) {
            registry.registerLoader(BaseAddon.class, platform.getAddons())
                    .registerLoader(BlockType.class,
                                    (t, object, cf) -> platform.getWorldHandle().createBlockData((String) object).getBlockType())
                    .registerLoader(BlockState.class, (t, object, cf) -> platform.getWorldHandle().createBlockData((String) object));
        }
    }
}
