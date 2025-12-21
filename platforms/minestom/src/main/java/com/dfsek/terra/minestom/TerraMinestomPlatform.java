package com.dfsek.terra.minestom;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.world.attribute.AmbientParticle;
import net.minestom.server.world.attribute.AmbientSounds;
import net.minestom.server.world.biome.BiomeEffects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.minestom.addon.MinestomAddon;
import com.dfsek.terra.minestom.api.BiomeFactory;
import com.dfsek.terra.minestom.api.TerraMinestomWorldBuilder;
import com.dfsek.terra.minestom.biome.MinestomBiomeLoader;
import com.dfsek.terra.minestom.biome.MinestomUserDefinedBiomeFactory;
import com.dfsek.terra.minestom.biome.MinestomUserDefinedBiomePool;
import com.dfsek.terra.minestom.config.BiomeAdditionsSoundTemplate;
import com.dfsek.terra.minestom.config.BiomeMoodSoundTemplate;
import com.dfsek.terra.minestom.config.BiomeParticleConfigTemplate;
import com.dfsek.terra.minestom.config.KeyLoader;
import com.dfsek.terra.minestom.config.RGBLikeLoader;
import com.dfsek.terra.minestom.config.SoundEventTemplate;
import com.dfsek.terra.minestom.entity.MinestomEntityType;
import com.dfsek.terra.minestom.item.MinestomItemHandle;
import com.dfsek.terra.minestom.world.MinestomChunkGeneratorWrapper;
import com.dfsek.terra.minestom.world.MinestomWorldHandle;


public final class TerraMinestomPlatform extends AbstractPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerraMinestomPlatform.class);
    private final WorldHandle worldHandle;
    private final ItemHandle itemHandle;
    private final TypeLoader<PlatformBiome> biomeTypeLoader;
    private final ArrayList<BaseAddon> platformAddons = new ArrayList<>(List.of(new MinestomAddon(this)));
    private final MinestomUserDefinedBiomePool biomePool;

    public TerraMinestomPlatform(WorldHandle worldHandle, ItemHandle itemHandle, TypeLoader<PlatformBiome> biomeTypeLoader,
                                 BiomeFactory biomeFactory, BaseAddon... extraAddons) {
        this.worldHandle = worldHandle;
        this.itemHandle = itemHandle;
        this.biomeTypeLoader = biomeTypeLoader;
        this.biomePool = new MinestomUserDefinedBiomePool(biomeFactory);
        this.platformAddons.addAll(List.of(extraAddons));
        load();
        getEventManager().callEvent(new PlatformInitializationEvent());
        initializeRegistry(); // Needs to be called before minecraft server bind
    }

    public TerraMinestomPlatform() {
        this(new MinestomWorldHandle(), new MinestomItemHandle(), new MinestomBiomeLoader(), new MinestomUserDefinedBiomeFactory());
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry
            .registerLoader(PlatformBiome.class, biomeTypeLoader)
            .registerLoader(RGBLike.class, new RGBLikeLoader())
            .registerLoader(Key.class, new KeyLoader())
            .registerLoader(EntityType.class,
                (TypeLoader<EntityType>) (annotatedType, o, configLoader, depthTracker) -> new MinestomEntityType((String) o))
            .registerLoader(BlockState.class,
                (TypeLoader<BlockState>) (annotatedType, o, configLoader, depthTracker) -> worldHandle.createBlockState((String) o))
            .registerLoader(AmbientParticle.class, BiomeParticleConfigTemplate::new)
            .registerLoader(AmbientSounds.Mood.class, BiomeMoodSoundTemplate::new)
            .registerLoader(AmbientSounds.Additions.class, BiomeAdditionsSoundTemplate::new)
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

    public void initializeRegistry() {
        getRawConfigRegistry()
            .forEach(pack -> biomePool.preloadBiomes(pack, pack.getBiomeProvider().getBiomes()));
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

    @Override
    protected Iterable<BaseAddon> platformAddon() {
        return platformAddons;
    }

    public TerraMinestomWorldBuilder worldBuilder(Instance instance) {
        return new TerraMinestomWorldBuilder(this, instance, biomePool);
    }

    public TerraMinestomWorldBuilder worldBuilder() {
        return worldBuilder(MinecraftServer.getInstanceManager().createInstanceContainer());
    }


    public static class Builder {
        private final List<BaseAddon> platformAddons = new ArrayList<>();
        private @Nullable WorldHandle worldHandle;
        private @Nullable ItemHandle itemHandle;
        private @Nullable TypeLoader<PlatformBiome> biomeTypeLoader;
        private @Nullable BiomeFactory biomeFactory;

        public Builder worldHandle(@Nullable WorldHandle worldHandle) {
            this.worldHandle = worldHandle;
            return this;
        }

        public Builder itemHandle(@Nullable ItemHandle itemHandle) {
            this.itemHandle = itemHandle;
            return this;
        }

        public Builder biomeTypeLoader(@Nullable TypeLoader<PlatformBiome> biomeTypeLoader) {
            this.biomeTypeLoader = biomeTypeLoader;
            return this;
        }

        public Builder addPlatformAddon(BaseAddon addon) {
            this.platformAddons.add(addon);
            return this;
        }

        public Builder biomeFactory(BiomeFactory biomeFactory) {
            this.biomeFactory = biomeFactory;
            return this;
        }

        public TerraMinestomPlatform build() {
            if(worldHandle == null) worldHandle = new MinestomWorldHandle();
            if(itemHandle == null) itemHandle = new MinestomItemHandle();
            if(biomeTypeLoader == null) biomeTypeLoader = new MinestomBiomeLoader();
            if(biomeFactory == null) biomeFactory = new MinestomUserDefinedBiomeFactory();

            return new TerraMinestomPlatform(
                worldHandle,
                itemHandle,
                biomeTypeLoader,
                biomeFactory,
                platformAddons.toArray(new BaseAddon[0])
            );
        }
    }
}