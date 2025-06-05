package com.dfsek.terra.minestom;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.minestom.biome.MinestomBiomeLoader;
import com.dfsek.terra.minestom.entity.MinestomEntityType;
import com.dfsek.terra.minestom.item.MinestomItemHandle;
import com.dfsek.terra.minestom.world.MinestomChunkGeneratorWrapper;
import com.dfsek.terra.minestom.world.MinestomWorldHandle;
import com.dfsek.terra.registry.master.ConfigRegistry.PackLoadFailuresException;

import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;



public final class MinestomPlatform extends AbstractPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinestomPlatform.class);
    private static MinestomPlatform INSTANCE = null;
    private final MinestomWorldHandle worldHandle = new MinestomWorldHandle();
    private final MinestomItemHandle itemHandle = new MinestomItemHandle();

    private MinestomPlatform() {
        load();
        getEventManager().callEvent(new PlatformInitializationEvent());
    }

    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry
            .registerLoader(PlatformBiome.class, new MinestomBiomeLoader())
            .registerLoader(EntityType.class, (TypeLoader<EntityType>) (annotatedType, o, configLoader, depthTracker) -> new MinestomEntityType((String) o))
            .registerLoader(BlockState.class, (TypeLoader<BlockState>) (annotatedType, o, configLoader, depthTracker) -> worldHandle.createBlockState((String) o));
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
        if (pathName == null) pathName = "./terra/";
        File file = new File(pathName);
        if(!file.exists()) file.mkdirs();
        return file;
    }

    public static MinestomPlatform getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new MinestomPlatform();
        }
        return INSTANCE;
    }
}