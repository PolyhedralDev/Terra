package com.dfsek.terra.addons.image.config.converter;

import com.dfsek.terra.addons.image.converter.ColorConverter;
import com.dfsek.terra.addons.image.converter.ExactColorConverter;
import com.dfsek.terra.addons.image.converter.mapping.ColorMapping;


public abstract class ExactColorConverterTemplate<T> implements ColorConverterTemplate<T> {
    
    protected abstract ColorMapping<T> getMapping();
    
    protected abstract T getFallback();
    
    @Override
    public ColorConverter<T> get() {
        return new ExactColorConverter<T>(getMapping().get(), getFallback());
    }
}
