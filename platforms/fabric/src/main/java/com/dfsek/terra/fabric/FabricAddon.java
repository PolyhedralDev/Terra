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
import com.dfsek.tectonic.exception.ConfigException;

import com.dfsek.terra.api.addon.BaseAddon;

import com.dfsek.terra.api.util.generic.pair.Pair.Mutable;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.world.biome.TerraBiome;
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
                             PreLoadCompatibilityOptions template = new PreLoadCompatibilityOptions();
                             try {
                                 event.loadTemplate(template);
                             } catch(ConfigException e) {
                                 logger.error("Error loading config template", e);
                             }
            
                             if(template.doRegistryInjection()) {
                                 logger.info("Injecting structures into Terra");
                
                                 BuiltinRegistries.CONFIGURED_FEATURE.getEntries().forEach(entry -> {
                                     if(!template.getExcludedRegistryFeatures().contains(entry.getKey().getValue())) {
                                         try {
                                             event.getPack()
                                                  .getCheckedRegistry(Tree.class)
                                                  .register(entry.getKey().getValue().toString(), (Tree) entry.getValue());
                                             logger.info("Injected ConfiguredFeature {} as Tree.", entry.getKey().getValue());
                                         } catch(DuplicateEntryException ignored) {
                                         }
                                     }
                                 });
                             }
                             templates.put(event.getPack(), Mutable.of(template, null));
                         })
                         .global();
        
        terraFabricPlugin.getEventManager()
                         .getHandler(FunctionalEventHandler.class)
                         .register(this, ConfigPackPostLoadEvent.class)
                         .then(event -> {
                             PostLoadCompatibilityOptions template = new PostLoadCompatibilityOptions();
            
                             try {
                                 event.loadTemplate(template);
                             } catch(ConfigException e) {
                                 logger.error("Error loading config template", e);
                             }
            
                             templates.get(event.getPack()).setRight(template);
                         })
                         .priority(100)
                         .global();
        
        terraFabricPlugin.getEventManager()
                         .getHandler(FunctionalEventHandler.class)
                         .register(this, BiomeRegistrationEvent.class)
                         .then(event -> {
                             logger.info("Registering biomes...");
            
                             Registry<Biome> biomeRegistry = event.getRegistryManager().get(Registry.BIOME_KEY);
                             terraFabricPlugin.getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
                                 pack.getCheckedRegistry(TerraBiome.class)
                                     .forEach((id, biome) -> {
                                         Identifier identifier = new Identifier("terra", FabricUtil.createBiomeID(pack, id));
                                         Biome fabricBiome = FabricUtil.createBiome(biome, pack, event.getRegistryManager());
                    
                                         FabricUtil.registerOrOverwrite(biomeRegistry, Registry.BIOME_KEY, identifier, fabricBiome);
                                     });
                             });
                             logger.info("Biomes registered.");
                         })
                         .global();
    }
    
    @Override
    public Version getVersion() {
        return VERSION;
    }
    
    
    private void injectTree(CheckedRegistry<Tree> registry, String id, ConfiguredFeature<?, ?> tree) {
        try {
            registry.register(id, (Tree) tree);
        } catch(DuplicateEntryException ignore) {
        }
    }
    
    public Map<ConfigPack, Mutable<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> getTemplates() {
        return templates;
    }
    
    @Override
    public String getID() {
        return "terra-fabric";
    }
}
