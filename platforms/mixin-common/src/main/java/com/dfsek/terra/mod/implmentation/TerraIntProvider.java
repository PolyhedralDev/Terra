package com.dfsek.terra.mod.implmentation;

import com.dfsek.terra.api.util.Range;

import com.dfsek.terra.mod.util.MinecraftAdapter;

import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.IntProviderType;
import net.minecraft.util.math.random.Random;

import java.util.HashMap;
import java.util.Map;


public class TerraIntProvider extends IntProvider {
    public static final Map<Class, IntProviderType> TERRA_RANGE_TYPE_TO_INT_PROVIDER_TYPE = new HashMap<>();
    
    public Range delegate;
    
    public TerraIntProvider(Range delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public int get(Random random) {
        return delegate.get(MinecraftAdapter.adapt(random));
    }
    
    @Override
    public int getMin() {
        return delegate.getMin();
    }
    
    @Override
    public int getMax() {
        return delegate.getMax();
    }
    
    @Override
    public IntProviderType<?> getType() {
        return TERRA_RANGE_TYPE_TO_INT_PROVIDER_TYPE.get(delegate.getClass());
    }
}
