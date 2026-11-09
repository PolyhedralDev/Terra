package com.dfsek.terra.mod.attribute;

import net.minecraft.resources.Identifier;

import java.util.Set;

import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;


final class TestBiome implements Biome {
    private final String id;
    private final int intId;
    private final PlatformBiome platformBiome;
    private final Context context = new Context();

    TestBiome(String id, int intId, Identifier parent) {
        this(id, intId, new ProtoPlatformBiome(parent));
    }

    TestBiome(String id, int intId, PlatformBiome platformBiome) {
        this.id = id;
        this.intId = intId;
        this.platformBiome = platformBiome;
    }

    ProtoPlatformBiome proto() {
        return (ProtoPlatformBiome) platformBiome;
    }

    @Override
    public PlatformBiome getPlatformBiome() {
        return platformBiome;
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public Set<String> getTags() {
        return Set.of();
    }

    @Override
    public int getIntID() {
        return intId;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "TestBiome[" + id + "]";
    }
}
