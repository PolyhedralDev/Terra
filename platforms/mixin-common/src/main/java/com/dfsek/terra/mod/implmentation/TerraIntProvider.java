package com.dfsek.terra.mod.implmentation;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.util.range.Range;
import com.dfsek.terra.mod.util.MinecraftAdapter;


public class TerraIntProvider implements IntProvider {
    public static final Map<Class, MapCodec<? extends IntProvider>> TERRA_RANGE_TYPE_TO_INT_PROVIDER_TYPE = new HashMap<>();

    public Range delegate;

    public TerraIntProvider(Range delegate) {
        this.delegate = delegate;
    }

    @Override
    public int sample(RandomSource random) {
        return delegate.get(MinecraftAdapter.adapt(random));
    }

    @Override
    public int minInclusive() {
        return delegate.getMin();
    }

    @Override
    public int maxInclusive() {
        return delegate.getMax();
    }

    @Override
    public MapCodec<? extends IntProvider> codec() {
        return TERRA_RANGE_TYPE_TO_INT_PROVIDER_TYPE.get(delegate.getClass());
    }
}
