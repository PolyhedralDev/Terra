package com.dfsek.terra.addons.image.config.converter;

import com.dfsek.terra.addons.image.converter.ClosestMatchColorConverter;
import com.dfsek.terra.addons.image.converter.ColorConverter;
import com.dfsek.terra.addons.image.converter.mapping.ColorMapping;


public abstract class ClosestColorConverterTemplate<T> implements ColorConverterTemplate<T> {
    
    protected abstract ColorMapping<T> getMapping();
    
    @Override
    public ColorConverter<T> get() {
        return new ClosestMatchColorConverter<T>(getMapping().get());
    }
}
