package com.dfsek.terra.fabric.codec;

import com.dfsek.terra.config.base.ConfigPack;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public class ConfigPackCodec implements Codec<ConfigPack> {
    @Override
    public <T> DataResult<Pair<ConfigPack, T>> decode(DynamicOps<T> ops, T input) {
        return null;
    }

    @Override
    public <T> DataResult<T> encode(ConfigPack input, DynamicOps<T> ops, T prefix) {
        return null;
    }
}
