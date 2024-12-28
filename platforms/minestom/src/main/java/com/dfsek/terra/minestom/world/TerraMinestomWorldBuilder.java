package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.config.ConfigPack;

import com.dfsek.terra.api.registry.CheckedRegistry;

import com.dfsek.terra.minestom.MinestomPlatform;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;

import java.util.Random;
import java.util.function.Function;


public class TerraMinestomWorldBuilder {
    private final Instance instance;
    private ConfigPack pack;
    private long seed = new Random().nextLong();

    private TerraMinestomWorldBuilder(Instance instance) { this.instance = instance; }

    public static TerraMinestomWorldBuilder from(Instance instance) {
        return new TerraMinestomWorldBuilder(instance);
    }

    public static TerraMinestomWorldBuilder builder() {
        return new TerraMinestomWorldBuilder(MinecraftServer.getInstanceManager().createInstanceContainer());
    }

    public TerraMinestomWorldBuilder pack(ConfigPack pack) {
        this.pack = pack;
        return this;
    }

    public TerraMinestomWorldBuilder packById(String id) {
        this.pack = MinestomPlatform
            .getInstance()
            .getConfigRegistry()
            .getByID(id)
            .orElseThrow();

        return this;
    }

    public TerraMinestomWorldBuilder findPack(Function<CheckedRegistry<ConfigPack>, ConfigPack> fn) {
        this.pack = fn.apply(MinestomPlatform.getInstance().getConfigRegistry());
        return this;
    }

    public TerraMinestomWorldBuilder seed(long seed) {
        this.seed = seed;
        return this;
    }

    public TerraMinestomWorld attach() {
        return new TerraMinestomWorld(instance, pack, seed);
    }
}
