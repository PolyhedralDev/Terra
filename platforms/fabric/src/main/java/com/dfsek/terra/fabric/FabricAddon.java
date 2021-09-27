package com.dfsek.terra.fabric;

import com.dfsek.tectonic.exception.ConfigException;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
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


@Addon("terra-fabric")
@Author("Terra")
@Version("1.0.0")
public final class FabricAddon extends TerraAddon {
    private final PlatformImpl terraFabricPlugin;
    private static final Logger logger = LoggerFactory.getLogger(FabricAddon.class);
    
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
    
    
    private void injectTree(CheckedRegistry<Tree> registry, String id, ConfiguredFeature<?, ?> tree) {
        try {
            registry.register(id, (Tree) tree);
        } catch(DuplicateEntryException ignore) {
        }
    }
    
    public Map<ConfigPack, Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions>> getTemplates() {
        return templates;
    }
}
