package com.dfsek.terra.mod.implmentation;

import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviderType;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.api.util.range.Range;
import com.dfsek.terra.mod.util.MinecraftAdapter;


public class TerraIntProvider extends IntProvider {
    public static final Map<Class, IntProviderType> TERRA_RANGE_TYPE_TO_INT_PROVIDER_TYPE = new HashMap<>();

    public Range delegate;

    public TerraIntProvider(Range delegate) {
        this.delegate = delegate;
    }

    @Override
    public int sample(RandomSource random) {
        return delegate.get(MinecraftAdapter.adapt(random));
    }

    @Override
    public int getMinValue() {
        return delegate.getMin();
    }

    @Override
    public int getMaxValue() {
        return delegate.getMax();
    }

    @Override
    public IntProviderType<?> getType() {
        return TERRA_RANGE_TYPE_TO_INT_PROVIDER_TYPE.get(delegate.getClass());
    }
}
