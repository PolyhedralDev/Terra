package com.dfsek.terra.minestom;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.loader.type.TypeLoader;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.minestom.addon.MinestomAddon;
import com.dfsek.terra.minestom.config.BiomeAdditionsSoundTemplate;
import com.dfsek.terra.minestom.config.BiomeParticleConfigTemplate;
import com.dfsek.terra.minestom.biome.MinestomBiomeLoader;
import com.dfsek.terra.minestom.config.KeyLoader;
import com.dfsek.terra.minestom.config.BiomeMoodSoundTemplate;
import com.dfsek.terra.minestom.config.RGBLikeLoader;
import com.dfsek.terra.minestom.config.SoundEventTemplate;
import com.dfsek.terra.minestom.entity.MinestomEntityType;
import com.dfsek.terra.minestom.item.MinestomItemHandle;
import com.dfsek.terra.minestom.world.MinestomChunkGeneratorWrapper;
import com.dfsek.terra.minestom.world.MinestomWorldHandle;

import com.dfsek.terra.minestom.world.TerraMinestomWorldBuilder;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.world.biome.BiomeEffects;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;


public final class TerraMinestomPlatform extends AbstractPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerraMinestomPlatform.class);
    private final WorldHandle worldHandle;
    private final ItemHandle itemHandle;
    private final TypeLoader<PlatformBiome> biomeTypeLoader;

    public TerraMinestomPlatform(WorldHandle worldHandle, ItemHandle itemHandle, TypeLoader<PlatformBiome> biomeTypeLoader) {
        this.worldHandle = worldHandle;
        this.itemHandle = itemHandle;
        this.biomeTypeLoader = biomeTypeLoader;
        load();
        getEventManager().callEvent(new PlatformInitializationEvent());
    }

    public TerraMinestomPlatform() {
        this(new MinestomWorldHandle(), new MinestomItemHandle(), new MinestomBiomeLoader());
    }

    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry
            .registerLoader(PlatformBiome.class, biomeTypeLoader)
            .registerLoader(RGBLike.class, new RGBLikeLoader())
            .registerLoader(Key.class, new KeyLoader())
            .registerLoader(EntityType.class, (TypeLoader<EntityType>) (annotatedType, o, configLoader, depthTracker) -> new MinestomEntityType((String) o))
            .registerLoader(BlockState.class, (TypeLoader<BlockState>) (annotatedType, o, configLoader, depthTracker) -> worldHandle.createBlockState((String) o))
            .registerLoader(BiomeEffects.Particle.class, BiomeParticleConfigTemplate::new)
            .registerLoader(BiomeEffects.MoodSound.class, BiomeMoodSoundTemplate::new)
            .registerLoader(BiomeEffects.AdditionsSound.class, BiomeAdditionsSoundTemplate::new)
            .registerLoader(SoundEvent.class, SoundEventTemplate::new);
    }

    @Override
    public boolean reload() {
        getTerraConfig().load(this);
        boolean succeed = loadConfigPacks();

        MinecraftServer.getInstanceManager().getInstances().forEach(world -> {
            if(world.generator() instanceof MinestomChunkGeneratorWrapper wrapper) {
                getConfigRegistry().get(wrapper.getPack().getRegistryKey()).ifPresent(pack -> {
                    wrapper.setPack(pack);
                    LOGGER.info("Replaced pack in chunk generator for instance {}", world.getUuid());
                });
            }
        });

        return succeed;
    }


    @Override
    public @NotNull WorldHandle getWorldHandle() {
        return worldHandle;
    }

    @Override
    public @NotNull ItemHandle getItemHandle() {
        return itemHandle;
    }

    @Override
    public @NotNull String platformName() {
        return "Minestom";
    }

    @Override
    public @NotNull File getDataFolder() {
        String pathName = System.getProperty("terra.datafolder");
        if(pathName == null) pathName = "./terra/";
        File file = new File(pathName);
        if(!file.exists()) file.mkdirs();
        return file;
    }

    public TerraMinestomWorldBuilder worldBuilder(Instance instance) {
        return new TerraMinestomWorldBuilder(this, instance);
    }

    public TerraMinestomWorldBuilder worldBuilder() {
        return new TerraMinestomWorldBuilder(this, MinecraftServer.getInstanceManager().createInstanceContainer());
    }

    @Override
    protected Iterable<BaseAddon> platformAddon() {
        return List.of(new MinestomAddon(this));
    }
}