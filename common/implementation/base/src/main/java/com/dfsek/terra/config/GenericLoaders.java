/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.config;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import com.dfsek.tectonic.api.TypeRegistry;

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
import com.dfsek.terra.config.loaders.VersionLoader;
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
                .registerLoader(Version.class, new VersionLoader())
                .registerLoader(MaterialSet.class, new MaterialSetLoader())
                .registerLoader(VersionRange.class, new VersionRangeLoader())
                .registerLoader(LinkedHashMap.class, new LinkedHashMapLoader());
        
        if(platform != null) {
            registry.registerLoader(BaseAddon.class, platform.getAddons())
                    .registerLoader(BlockType.class, (type, object, configLoader, depthTracker) -> platform
                            .getWorldHandle().createBlockState((String) object).getBlockType())
                    .registerLoader(BlockState.class, (type, object, configLoader, depthTracker) -> platform
                            .getWorldHandle().createBlockState((String) object));
        }
    }
}
