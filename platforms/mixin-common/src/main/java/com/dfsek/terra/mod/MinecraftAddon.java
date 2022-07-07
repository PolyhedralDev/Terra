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

package com.dfsek.terra.mod;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.mod.config.PostLoadCompatibilityOptions;
import com.dfsek.terra.mod.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;


public abstract class MinecraftAddon implements BaseAddon {
    private static final Version VERSION = Versions.getVersion(1, 0, 0);
    private static final Logger logger = LoggerFactory.getLogger(MinecraftAddon.class);
    private final ModPlatform modPlatform;
    
    public MinecraftAddon(ModPlatform modPlatform) {
        this.modPlatform = modPlatform;
    }
    
    @Override
    public void initialize() {
        modPlatform.getEventManager()
                   .getHandler(FunctionalEventHandler.class)
                   .register(this, ConfigPackPreLoadEvent.class)
                   .then(event -> event.getPack().getContext().put(event.loadTemplate(new PreLoadCompatibilityOptions())))
                   .global();
        
        modPlatform.getEventManager()
                   .getHandler(FunctionalEventHandler.class)
                   .register(this, ConfigPackPostLoadEvent.class)
                   .then(event -> event.getPack().getContext().put(event.loadTemplate(new PostLoadCompatibilityOptions())))
                   .priority(100)
                   .global();
        
        modPlatform.getEventManager()
                   .getHandler(FunctionalEventHandler.class)
                   .register(this, ConfigurationLoadEvent.class)
                   .then(event -> {
                       if(event.is(Biome.class)) {
                           event.getLoadedObject(Biome.class).getContext().put(event.load(new VanillaBiomeProperties()));
                       }
                   })
                   .global();
    }
    
    @Override
    public Version getVersion() {
        return VERSION;
    }
}
