package com.dfsek.terra.addons.attribute.minecraft;

import java.util.regex.Pattern;

import com.dfsek.terra.api.registry.key.RegistryKey;


public final class MinecraftIds {
    private static final Pattern NAMESPACE = Pattern.compile("[a-z0-9_.-]+");
    private static final Pattern PATH = Pattern.compile("[a-z0-9_./-]+");

    private MinecraftIds() { }

    public static RegistryKey requireLowerCase(RegistryKey key) {
        return requireLowerCase(key, key.toString());
    }

    public static RegistryKey requireLowerCase(RegistryKey key, String written) {
        if(!NAMESPACE.matcher(key.getNamespace()).matches()) {
            throw new IllegalArgumentException(
                "invalid ID \"" + written + "\": the namespace must be non-empty and use only a-z, 0-9, _, . and -");
        }
        if(!PATH.matcher(key.getID()).matches()) {
            throw new IllegalArgumentException(
                "invalid ID \"" + written + "\": the path must be non-empty and use only a-z, 0-9, _, ., / and -");
        }
        return key;
    }
}
