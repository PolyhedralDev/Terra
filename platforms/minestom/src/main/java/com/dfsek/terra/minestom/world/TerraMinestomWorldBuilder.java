package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.config.ConfigPack;

import com.dfsek.terra.api.registry.CheckedRegistry;

import com.dfsek.terra.minestom.MinestomPlatform;
import com.dfsek.terra.minestom.api.BlockEntityFactory;
import com.dfsek.terra.minestom.api.EntityFactory;
import com.dfsek.terra.minestom.block.DefaultBlockEntityFactory;
import com.dfsek.terra.minestom.entity.DefaultEntityFactory;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;

import java.util.Random;
import java.util.function.Function;


public class TerraMinestomWorldBuilder {
    private final Instance instance;
    private ConfigPack pack;
    private long seed = new Random().nextLong();
    private EntityFactory entityFactory = new DefaultEntityFactory();
    private BlockEntityFactory blockEntityFactory;

    private TerraMinestomWorldBuilder(Instance instance) {
        this.instance = instance;
        this.blockEntityFactory = new DefaultBlockEntityFactory(instance);
    }

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
        this.pack = MinestomPlatform.getInstance().getConfigRegistry().getByID(id).orElseThrow();

        return this;
    }

    public TerraMinestomWorldBuilder findPack(Function<CheckedRegistry<ConfigPack>, ConfigPack> fn) {
        this.pack = fn.apply(MinestomPlatform.getInstance().getConfigRegistry());
        return this;
    }

    public TerraMinestomWorldBuilder defaultPack() {
        return this.packById("OVERWORLD");
    }

    public TerraMinestomWorldBuilder seed(long seed) {
        this.seed = seed;
        return this;
    }

    public TerraMinestomWorldBuilder entityFactory(EntityFactory factory) {
        this.entityFactory = factory;
        return this;
    }

    public TerraMinestomWorldBuilder blockEntityFactory(BlockEntityFactory factory) {
        this.blockEntityFactory = factory;
        return this;
    }

    public TerraMinestomWorld attach() {
        return new TerraMinestomWorld(instance, pack, seed, entityFactory, blockEntityFactory);
    }
}
