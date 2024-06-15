package org.allaymc.terra.allay;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;

import org.allaymc.api.server.Server;
import org.allaymc.terra.allay.handle.AllayItemHandle;
import org.allaymc.terra.allay.handle.AllayWorldHandle;
import org.jetbrains.annotations.NotNull;

import java.io.File;


/**
 * Terra Project 2024/6/15
 *
 * @author daoge_cmd
 */
public class AllayPlatform extends AbstractPlatform {

    protected static final AllayWorldHandle ALLAY_WORLD_HANDLE = new AllayWorldHandle();
    protected static final AllayItemHandle ALLAY_ITEM_HANDLE = new AllayItemHandle();

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
}
