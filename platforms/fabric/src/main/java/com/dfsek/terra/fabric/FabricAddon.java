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

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.fabric.config.PostLoadCompatibilityOptions;
import com.dfsek.terra.fabric.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.fabric.event.BiomeRegistrationEvent;
import com.dfsek.terra.fabric.util.FabricUtil;


public final class FabricAddon implements BaseAddon {
    private static final Version VERSION = Versions.getVersion(1, 0, 0);
    private final PlatformImpl terraFabricPlugin;
    private final Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> templates = new HashMap<>();
    
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
                                 e.printStackTrace();
                             }
            
                             if(template.doRegistryInjection()) {
                                 BuiltinRegistries.CONFIGURED_FEATURE.getEntries().forEach(entry -> {
                                     if(!template.getExcludedRegistryFeatures().contains(entry.getKey().getValue())) {
                                         try {
                                             event.getPack().getCheckedRegistry(Tree.class).register(entry.getKey().getValue().toString(),
                                                                                                     (Tree) entry.getValue());
                                             terraFabricPlugin.getDebugLogger().info(
                                                     "Injected ConfiguredFeature " + entry.getKey().getValue() + " as Tree.");
                                         } catch(DuplicateEntryException ignored) {
                                         }
                                     }
                                 });
                             }
                             templates.put(event.getPack(), Pair.of(template, null));
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
                                 e.printStackTrace();
                             }
            
                             templates.get(event.getPack()).setRight(template);
                         })
                         .priority(100)
                         .global();
        
        terraFabricPlugin.getEventManager()
                         .getHandler(FunctionalEventHandler.class)
                         .register(this, BiomeRegistrationEvent.class)
                         .then(event -> {
                             terraFabricPlugin.logger().info("Registering biomes...");
                             Registry<Biome> biomeRegistry = event.getRegistryManager().get(Registry.BIOME_KEY);
                             terraFabricPlugin.getConfigRegistry().forEach(pack -> pack.getCheckedRegistry(TerraBiome.class)
                                                                                       .forEach(
                                                                                               (id, biome) -> FabricUtil.registerOrOverwrite(
                                                                                                       biomeRegistry, Registry.BIOME_KEY,
                                                                                                       new Identifier("terra",
                                                                                                                      FabricUtil.createBiomeID(
                                                                                                                              pack, id)),
                                                                                                       FabricUtil.createBiome(biome, pack,
                                                                                                                              event.getRegistryManager())))); // Register all Terra biomes.
                             terraFabricPlugin.logger().info("Biomes registered.");
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
    
    public Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> getTemplates() {
        return templates;
    }
    
    @Override
    public String getID() {
        return "terra-fabric";
    }
}
