package com.dfsek.terra.lifecycle;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.parser.tokenizer.ParseException;
import net.minecraft.MinecraftVersion;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.dfsek.terra.addon.EphemeralAddon;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.mod.CommonPlatform;
import com.dfsek.terra.mod.ModPlatform;
import com.dfsek.terra.mod.generation.MinecraftChunkGeneratorWrapper;


public abstract class LifecyclePlatform extends ModPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger(LifecyclePlatform.class);
    private static MinecraftServer server;
    
    private static final AtomicReference<Registry<Biome>> BIOMES = new AtomicReference<>();
    private static final AtomicReference<Registry<DimensionType>> DIMENSIONS = new AtomicReference<>();
    private static final AtomicReference<Registry<ChunkGeneratorSettings>> SETTINGS = new AtomicReference<>();
    private static final AtomicReference<Registry<MultiNoiseBiomeSourceParameterList>> NOISE = new AtomicReference<>();
    
    public LifecyclePlatform() {
        CommonPlatform.initialize(this);
        load();
    }
    
    @Override
    public MinecraftServer getServer() {
        return server;
    }
    
    public static void setServer(MinecraftServer server) {
        LifecyclePlatform.server = server;
    }
    
    @Override
    public boolean reload() {
        getTerraConfig().load(this);
        getRawConfigRegistry().clear();
        boolean succeed = getRawConfigRegistry().loadAll(this);
        
        
        if(server != null) {
            server.reloadResources(server.getDataPackManager().getNames()).exceptionally(throwable -> {
                LOGGER.warn("Failed to execute reload", throwable);
                return null;
            }).join();
            server.getWorlds().forEach(world -> {
                if(world.getChunkManager().getChunkGenerator() instanceof MinecraftChunkGeneratorWrapper chunkGeneratorWrapper) {
                    getConfigRegistry().get(chunkGeneratorWrapper.getPack().getRegistryKey()).ifPresent(pack -> {
                        chunkGeneratorWrapper.setPack(pack);
                        LOGGER.info("Replaced pack in chunk generator for world {}", world);
                    });
                }
            });
        }
        return succeed;
    }
    
    public static void setRegistries(Registry<Biome> biomeRegistry,
                                     Registry<DimensionType> dimensionTypeRegistry,
                                     Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry,
                                     Registry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterListRegistry) {
        BIOMES.set(biomeRegistry);
        DIMENSIONS.set(dimensionTypeRegistry);
        SETTINGS.set(chunkGeneratorSettingsRegistry);
        NOISE.set(multiNoiseBiomeSourceParameterListRegistry);
    }
    
    @Override
    protected Iterable<BaseAddon> platformAddon() {
        List<BaseAddon> addons = new ArrayList<>();
        
        super.platformAddon().forEach(addons::add);
        
        String mcVersion = MinecraftVersion.CURRENT.getName();
        try {
            addons.add(new EphemeralAddon(Versions.parseVersion(mcVersion), "minecraft"));
        } catch(ParseException e) {
            try {
                addons.add(new EphemeralAddon(Versions.parseVersion(mcVersion + ".0"), "minecraft"));
            } catch(ParseException ex) {
                LOGGER.warn("Failed to parse Minecraft version", e);
            }
        }
        
        addons.addAll(getPlatformMods());
        
        return addons;
    }
    
    @Override
    public Registry<DimensionType> dimensionTypeRegistry() {
        return DIMENSIONS.get();
    }
    
    @Override
    public Registry<Biome> biomeRegistry() {
        return BIOMES.get();
    }
    
    @Override
    public Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry() {
        return SETTINGS.get();
    }
    
    @Override
    public Registry<MultiNoiseBiomeSourceParameterList> multiNoiseBiomeSourceParameterListRegistry() {
        return NOISE.get();
    }
    
    protected abstract Collection<BaseAddon> getPlatformMods();
}
