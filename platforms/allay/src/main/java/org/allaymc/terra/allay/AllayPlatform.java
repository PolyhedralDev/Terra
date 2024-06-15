package org.allaymc.terra.allay;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;

import org.jetbrains.annotations.NotNull;

import java.io.File;


/**
 * Terra Project 2024/6/15
 *
 * @author daoge_cmd
 */
public class AllayPlatform extends AbstractPlatform {

    @Override
    public boolean reload() {
        return false;
    }

    @Override
    public @NotNull String platformName() {
        return "Allay";
    }

    @Override
    public @NotNull WorldHandle getWorldHandle() {
        // TODO
        return null;
    }

    @Override
    public @NotNull File getDataFolder() {
        return TerraAllayPlugin.INSTANCE.getPluginContainer().dataFolder().toFile();
    }

    @Override
    public @NotNull ItemHandle getItemHandle() {
        // TODO
        return null;
    }
}
