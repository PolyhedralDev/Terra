package com.dfsek.terra.minestom.api;

import net.minestom.server.instance.Instance;

import java.util.Random;
import java.util.function.Function;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.minestom.TerraMinestomPlatform;
import com.dfsek.terra.minestom.biome.MinestomUserDefinedBiomeFactory;
import com.dfsek.terra.minestom.block.DefaultBlockEntityFactory;
import com.dfsek.terra.minestom.entity.DefaultEntityFactory;
import com.dfsek.terra.minestom.world.TerraMinestomWorld;


public class TerraMinestomWorldBuilder {
    private final TerraMinestomPlatform platform;
    private final Instance instance;
    private ConfigPack pack;
    private long seed = new Random().nextLong();
    private EntityFactory entityFactory = new DefaultEntityFactory();
    private BlockEntityFactory blockEntityFactory;
    private BiomeFactory biomeFactory = new MinestomUserDefinedBiomeFactory();

    public TerraMinestomWorldBuilder(TerraMinestomPlatform platform, Instance instance) {
        this.platform = platform;
        this.instance = instance;
        this.blockEntityFactory = new DefaultBlockEntityFactory(instance);
    }

    public TerraMinestomWorldBuilder pack(ConfigPack pack) {
        this.pack = pack;
        return this;
    }

    public TerraMinestomWorldBuilder packById(String id) {
        this.pack = platform.getConfigRegistry().getByID(id).orElseThrow();

        return this;
    }

    public TerraMinestomWorldBuilder findPack(Function<CheckedRegistry<ConfigPack>, ConfigPack> fn) {
        this.pack = fn.apply(platform.getConfigRegistry());
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

    public TerraMinestomWorldBuilder biomeFactory(BiomeFactory factory) {
        this.biomeFactory = factory;
        return this;
    }

    public TerraMinestomWorld attach() {
        return new TerraMinestomWorld(platform, instance, pack, seed, entityFactory, blockEntityFactory, biomeFactory);
    }
}
