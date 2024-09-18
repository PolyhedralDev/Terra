package org.allaymc.terra.allay;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.depth.DepthTracker;
import com.dfsek.tectonic.api.exception.LoadException;
import org.allaymc.api.server.Server;
import org.allaymc.api.world.biome.BiomeId;
import org.allaymc.terra.allay.delegate.AllayBiome;
import org.allaymc.terra.allay.handle.AllayItemHandle;
import org.allaymc.terra.allay.handle.AllayWorldHandle;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.biome.PlatformBiome;


/**
 * @author daoge_cmd
 */
public class AllayPlatform extends AbstractPlatform {

    protected static final AllayWorldHandle ALLAY_WORLD_HANDLE = new AllayWorldHandle();
    protected static final AllayItemHandle ALLAY_ITEM_HANDLE = new AllayItemHandle();

    public AllayPlatform() {
        load();
    }

    @Override
    public boolean reload() {
        // TODO: Implement reload
        return false;
    }

    @Override
    public @NotNull String platformName() {
        return "Allay";
    }

    @Override
    public @NotNull WorldHandle getWorldHandle() {
        return ALLAY_WORLD_HANDLE;
    }

    @Override
    public @NotNull ItemHandle getItemHandle() {
        return ALLAY_ITEM_HANDLE;
    }

    @Override
    public @NotNull File getDataFolder() {
        return TerraAllayPlugin.INSTANCE.getPluginContainer().dataFolder().toFile();
    }

    @Override
    public void runPossiblyUnsafeTask(@NotNull Runnable task) {
        Server.getInstance().getScheduler().runLater(Server.getInstance(), task);
    }

    @Override
    public void register(TypeRegistry registry) {
        super.register(registry);
        registry.registerLoader(BlockState.class, (type, o, loader, depthTracker) -> ALLAY_WORLD_HANDLE.createBlockState((String) o))
            .registerLoader(PlatformBiome.class, (type, o, loader, depthTracker) -> parseBiome((String) o, depthTracker));
    }

    protected AllayBiome parseBiome(String id, DepthTracker depthTracker) throws LoadException {
        if(!id.startsWith("minecraft:")) throw new LoadException("Invalid biome identifier " + id, depthTracker);
        return new AllayBiome(BiomeId.fromId(Mapping.biomeIdJeToBe(id)));
    }
}
