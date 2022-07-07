package com.dfsek.terra.mod;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.biome.Biome.TemperatureModifier;
import net.minecraft.world.biome.BiomeEffects.GrassColorModifier;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.WorldPreset;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.mod.config.BiomeAdditionsSoundTemplate;
import com.dfsek.terra.mod.config.BiomeMoodSoundTemplate;
import com.dfsek.terra.mod.config.BiomeParticleConfigTemplate;
import com.dfsek.terra.mod.config.EntityTypeTemplate;
import com.dfsek.terra.mod.config.MusicSoundTemplate;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.config.SoundEventTemplate;
import com.dfsek.terra.mod.config.SpawnCostConfig;
import com.dfsek.terra.mod.config.SpawnEntryTemplate;
import com.dfsek.terra.mod.config.SpawnGroupTemplate;
import com.dfsek.terra.mod.config.SpawnSettingsTemplate;
import com.dfsek.terra.mod.config.SpawnTypeConfig;
import com.dfsek.terra.mod.config.VillagerTypeTemplate;
import com.dfsek.terra.mod.handle.MinecraftItemHandle;
import com.dfsek.terra.mod.handle.MinecraftWorldHandle;
import com.dfsek.terra.mod.util.PresetUtil;


public abstract class ModPlatform extends AbstractPlatform {
    private final ItemHandle itemHandle = new MinecraftItemHandle();
    private final WorldHandle worldHandle = new MinecraftWorldHandle();

    public abstract MinecraftServer getServer();
    
    public void registerWorldTypes(BiConsumer<Identifier, WorldPreset> registerFunction) {
        getRawConfigRegistry()
                .forEach(pack -> PresetUtil.createDefault(pack).apply(registerFunction));
    }
    
    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry.registerLoader(PlatformBiome.class, (type, o, loader, depthTracker) -> parseBiome((String) o, depthTracker))
                .registerLoader(Identifier.class, (type, o, loader, depthTracker) -> {
                    Identifier identifier = Identifier.tryParse((String) o);
                    if(identifier == null)
                        throw new LoadException("Invalid identifier: " + o, depthTracker);
                    return identifier;
                })
                .registerLoader(Precipitation.class, (type, o, loader, depthTracker) -> Precipitation.valueOf(((String) o).toUpperCase(
                        Locale.ROOT)))
                .registerLoader(GrassColorModifier.class,
                                (type, o, loader, depthTracker) -> GrassColorModifier.valueOf(((String) o).toUpperCase(
                                        Locale.ROOT)))
                .registerLoader(GrassColorModifier.class,
                                (type, o, loader, depthTracker) -> TemperatureModifier.valueOf(((String) o).toUpperCase(
                                        Locale.ROOT)))
                .registerLoader(BiomeParticleConfig.class, BiomeParticleConfigTemplate::new)
                .registerLoader(SoundEvent.class, SoundEventTemplate::new)
                .registerLoader(BiomeMoodSound.class, BiomeMoodSoundTemplate::new)
                .registerLoader(BiomeAdditionsSound.class, BiomeAdditionsSoundTemplate::new)
                .registerLoader(MusicSound.class, MusicSoundTemplate::new)
                .registerLoader(EntityType.class, EntityTypeTemplate::new)
                .registerLoader(SpawnCostConfig.class, SpawnCostConfig::new)
                .registerLoader(SpawnEntry.class, SpawnEntryTemplate::new)
                .registerLoader(SpawnGroup.class, SpawnGroupTemplate::new)
                .registerLoader(SpawnTypeConfig.class, SpawnTypeConfig::new)
                .registerLoader(SpawnSettings.class, SpawnSettingsTemplate::new)
                .registerLoader(VillagerType.class, VillagerTypeTemplate::new);
    }
    
    private ProtoPlatformBiome parseBiome(String id, DepthTracker tracker) throws LoadException {
        Identifier identifier = Identifier.tryParse(id);
        if(BuiltinRegistries.BIOME.get(identifier) == null) throw new LoadException("Invalid Biome ID: " + identifier, tracker); // failure.
        return new ProtoPlatformBiome(identifier);
    }
    
    @Override
    protected Iterable<BaseAddon> platformAddon() {
        return List.of(getPlatformAddon());
    }
    
    protected abstract BaseAddon getPlatformAddon();
    
    @Override
    public @NotNull WorldHandle getWorldHandle() {
        return worldHandle;
    }
    
    @Override
    public @NotNull ItemHandle getItemHandle() {
        return itemHandle;
    }
}
