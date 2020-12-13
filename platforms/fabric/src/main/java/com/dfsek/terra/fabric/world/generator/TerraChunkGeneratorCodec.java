package com.dfsek.terra.fabric.world.generator;

import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Optional;

public class TerraChunkGeneratorCodec implements Codec<TerraChunkGenerator> {
    private final TerraPlugin main;

    public TerraChunkGeneratorCodec(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public <T> DataResult<Pair<TerraChunkGenerator, T>> decode(DynamicOps<T> ops, T input) {
        Optional<String> s = ops.getStringValue(input).get().left();
        if(!s.isPresent()) return DataResult.error("No data present");
        if(main.getRegistry().contains(s.get())) {
            return DataResult.success(new Pair<>(new TerraChunkGenerator(main.getRegistry().get(s.get()), main), input));
        }
        return null;
    }

    @Override
    public <T> DataResult<T> encode(TerraChunkGenerator input, DynamicOps<T> ops, T prefix) {
        return null;
    }
}
