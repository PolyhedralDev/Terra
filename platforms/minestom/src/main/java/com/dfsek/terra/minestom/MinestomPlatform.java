package com.dfsek.terra.minestom;

import com.dfsek.tectonic.api.TypeRegistry;

import com.dfsek.tectonic.api.loader.type.TypeLoader;

import com.dfsek.terra.AbstractPlatform;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.api.handle.ItemHandle;
import com.dfsek.terra.api.handle.WorldHandle;

import com.dfsek.terra.api.world.biome.PlatformBiome;

import com.dfsek.terra.minestom.item.MinestomItemHandle;

import com.dfsek.terra.minestom.world.MinestomWorldHandle;

import org.jetbrains.annotations.NotNull;

import java.io.File;


public final class MinestomPlatform extends AbstractPlatform {
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
        registry.registerLoader(PlatformBiome.class, (TypeLoader<PlatformBiome>) (annotatedType, o, configLoader, depthTracker) -> () -> o);
    }

    @Override
    public boolean reload() {
        return false;
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
        File file = new File("./terra/");
        if (!file.exists()) file.mkdirs();
        return file;
    }


    public static MinestomPlatform getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MinestomPlatform();
        }
        return INSTANCE;
    }
}
