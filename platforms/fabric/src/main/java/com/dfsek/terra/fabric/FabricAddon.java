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

package com.dfsek.terra.fabric;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;
import com.dfsek.tectonic.api.exception.ConfigException;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.generic.pair.Pair.Mutable;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.fabric.config.PostLoadCompatibilityOptions;
import com.dfsek.terra.fabric.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.fabric.event.BiomeRegistrationEvent;
import com.dfsek.terra.fabric.util.FabricUtil;


public final class FabricAddon implements BaseAddon {
    private static final Version VERSION = Versions.getVersion(1, 0, 0);
    private final PlatformImpl terraFabricPlugin;
    private static final Logger logger = LoggerFactory.getLogger(FabricAddon.class);
    
    private final Map<ConfigPack, Mutable<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> templates = new HashMap<>();
    
    public FabricAddon(PlatformImpl terraFabricPlugin) {
        this.terraFabricPlugin = terraFabricPlugin;
    }
    
    @Override
    public void initialize() {
        terraFabricPlugin.getEventManager()
                         .getHandler(FunctionalEventHandler.class)
                         .register(this, ConfigPackPreLoadEvent.class)
                         .then(event -> {
                             try {
                                 PreLoadCompatibilityOptions template = event.loadTemplate(new PreLoadCompatibilityOptions());
                                 templates.put(event.getPack(), Mutable.of(template, null));
                             } catch(ConfigException e) {
                                 logger.error("Error loading config template", e);
                             }
                         })
                         .global();
        
        terraFabricPlugin.getEventManager()
                         .getHandler(FunctionalEventHandler.class)
                         .register(this, ConfigPackPostLoadEvent.class)
                         .then(event -> {
                             try {
                                 templates.get(event.getPack()).setRight(event.loadTemplate(new PostLoadCompatibilityOptions()));
                             } catch(ConfigException e) {
                                 logger.error("Error loading config template", e);
                             }
                         })
                         .priority(100)
                         .global();
        
        terraFabricPlugin.getEventManager()
                         .getHandler(FunctionalEventHandler.class)
                         .register(this, BiomeRegistrationEvent.class)
                         .then(event -> {
                             logger.info("Registering biomes...");
            
                             Registry<net.minecraft.world.biome.Biome> biomeRegistry = event.getRegistryManager().get(Registry.BIOME_KEY);
                             terraFabricPlugin.getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
                                 pack.getCheckedRegistry(Biome.class)
                                     .forEach((id, biome) -> FabricUtil.registerBiome(biome, pack, event.getRegistryManager(), id));
                             });
                             logger.info("Biomes registered.");
                         })
                         .global();
    }
    
    @Override
    public Version getVersion() {
        return VERSION;
    }
    
    
    public Map<ConfigPack, Mutable<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> getTemplates() {
        return templates;
    }
    
    @Override
    public String getID() {
        return "terra-fabric";
    }
}
