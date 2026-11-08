package com.dfsek.terra.addons.attribute.minecraft;

import java.util.Optional;
import java.util.OptionalInt;

import com.dfsek.terra.api.world.attribute.RawValue;


public record IntProviderValue(OptionalInt constant, Optional<RawValue> provider) {
    public IntProviderValue {
        if(constant.isPresent() == provider.isPresent()) {
            throw new IllegalArgumentException("an int provider value needs exactly one of a constant and a provider");
        }
    }
}
